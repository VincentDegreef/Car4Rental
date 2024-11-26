import Image from "next/image";
import { useRouter } from "next/router";
import React from "react"; 

const Language: React.FC = () => {
    const router = useRouter();
    const { locale, pathname, asPath, query } = router;
  
    return (
        <div className="flex gap-3">
            {locale !== "nl" &&
                <button onClick={() => router.push({ pathname, query }, asPath, { locale: "nl" })}>
                    <Image src="/nl.png" alt="Nederlands" width={30} height={30} className="rounded-xl"/>
                </button>
            }
            {locale !== "en" &&
                <button onClick={() => router.push({ pathname, query }, asPath, { locale: "en" })}>
                    <Image src="/en.png" alt="English" width={30} height={30} className="rounded-xl"/>
                </button>
            }
            {locale !== "fr" &&
                <button onClick={() => router.push({ pathname, query }, asPath, { locale: "fr" })}>
                    <Image src="/fr.png" alt="Francais" width={30} height={30} className="rounded-xl"/>
                </button>
            }
        </div>
        
      );}

  export default Language;