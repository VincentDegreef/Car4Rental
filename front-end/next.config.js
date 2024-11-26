const { i18n } = require('next-i18next');

const nextConfig = {
  reactStrictMode: true,
  output: 'standalone',
  i18n: {
    locales: ['en', 'nl', 'fr'],
    defaultLocale: 'en',
  },
};

module.exports = nextConfig;
