import React, { useState, useEffect } from 'react';
import GreetingService from "@/services/GreetingService"; 
import Header  from "@/components/Header"; 
import { Greeting } from '@/types';
import { serverSideTranslations } from 'next-i18next/serverSideTranslations';

export const getServerSideProps = async (context: any) => {
  const {locale} = context;

  return {
    props: {
      ...(await serverSideTranslations(locale ?? "en", ['common']))
    },
  };
};

const Hello: React.FC = () => {
  const [greetings, setGreetings] = useState<Greeting[]>([]);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    GreetingService.getGreetings()
      .then((fetchedGreetings) => {
        
        if (Array.isArray(fetchedGreetings)) {
          setGreetings(fetchedGreetings);
          setError(null); 
        } else {
          setError('Failed to fetch greetings. Please try again later.');
          console.error('Received data is not an array:', fetchedGreetings);
        }
      })
      .catch((error) => {
        setError('Failed to fetch greetings. Please try again later.');
        console.error(error);

      });
  }, []);

  return (
    <div>
      <Header />
      <div className="flex flex-col items-center justify-center h-screen">
        {error ? (
          <p>Error: {error}</p>
        ) : (
          greetings && greetings.map((greeting) => (
            <div key={greeting.id} className="text-center">
              <h2 className="text-2xl font-bold">{greeting.message}</h2>
            </div>
          ))
        )}
      </div>
    </div>
  );
};

export default Hello;
