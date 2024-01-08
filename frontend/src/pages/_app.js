import '@/styles/globals.css'
import '../styles/styles.css';
import Head from 'next/head';
import 'bootstrap/dist/css/bootstrap.min.css';
export default function App({ Component, pageProps }) {
  return (
    <>
      <Head>
        <link
          rel="stylesheet"
          href="https://fonts.googleapis.com/css2?family=Rubik+Mono+One&display=swap"
        />
      </Head>
      <Component {...pageProps} />
    </>
  );
}
