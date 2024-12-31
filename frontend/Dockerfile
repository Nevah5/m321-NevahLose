FROM node:20-alpine3.19 as build

ENV NODE_ENV=PRODUCTION

WORKDIR /usr/src/app

COPY . .

RUN npm install && \
  npm run build

FROM nginx:1.27.2-alpine

COPY --from=build /usr/src/app/dist/ /usr/share/nginx/html/

RUN echo "server {" > /etc/nginx/conf.d/default.conf && \
  echo "  listen 80;" >> /etc/nginx/conf.d/default.conf && \
  echo "  server_name localhost;" >> /etc/nginx/conf.d/default.conf && \
  echo "  charset utf-8;" >> /etc/nginx/conf.d/default.conf && \
  echo "  root /usr/share/nginx/html;" >> /etc/nginx/conf.d/default.conf && \
  echo "  index index.html index.htm;" >> /etc/nginx/conf.d/default.conf && \
  echo "  location / {" >> /etc/nginx/conf.d/default.conf && \
  echo "    root /usr/share/nginx/html;" >> /etc/nginx/conf.d/default.conf && \
  echo "    try_files \$uri /index.html;" >> /etc/nginx/conf.d/default.conf && \
  echo "  }" >> /etc/nginx/conf.d/default.conf && \
  echo "  include /etc/nginx/mime.types;" >> /etc/nginx/conf.d/default.conf && \
  echo "  types {" >> /etc/nginx/conf.d/default.conf && \
  echo "    image/webp webp;" >> /etc/nginx/conf.d/default.conf && \
  echo "  }" >> /etc/nginx/conf.d/default.conf && \
  echo "}" >> /etc/nginx/conf.d/default.conf

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]