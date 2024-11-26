import "@/styles/globals.css";
import next from "next";
import { appWithTranslation } from "next-i18next";
import type { AppProps } from "next/app";
import nextI18NextConfig from '../next-i18next.config.js';

const App = ({ Component, pageProps }: AppProps) => {
  return <Component {...pageProps} />;
}

export default appWithTranslation(App, nextI18NextConfig)
