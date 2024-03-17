#!/bin/bash

echo "Configuración básica de HAProxy"

# Eliminar todas las aplicaciones Java.
killall java

# Crear una copia del archivo de configuración actual.
if [ ! -e "/etc/haproxy/haproxy.cfg.original" ]; then
  echo "Creando archivo de backup"
  sudo cp /etc/haproxy/haproxy.cfg /etc/haproxy/haproxy.cfg.original
fi

# Copiar el archivo de configuración de HAProxy.
sudo cp Practicas-ICC354/practica-6/haproxy.cfg /etc/haproxy/haproxy.cfg

# Instalar Docker
sudo apt install -y docker-compose

# Instalar Certbot
sudo apt-get remove -y certbot
sudo apt-get update
sudo apt-get install -y software-properties-common
sudo add-apt-repository -y ppa:certbot/certbot
sudo apt-get update
sudo apt-get install -y certbot

# Dominio para el que se generará el certificado SSL
DOMAIN="p6.turnos.do"

# Directorio donde se guardarán los archivos generados por Let's Encrypt
LE_DIR="/etc/letsencrypt/live/$DOMAIN"

# Directorio donde se guardará el certificado y la clave privada
CERT_DIR="/etc/ssl/certs"

# Comando para obtener el certificado SSL utilizando certbot de Let's Encrypt
sudo certbot certonly --standalone --preferred-challenges http -d $DOMAIN

# Copiar el certificado y la clave privada al directorio de certificados
sudo mkdir -p $CERT_DIR
sudo cp $LE_DIR/fullchain.pem $CERT_DIR/domain.pem
sudo cp $LE_DIR/privkey.pem $CERT_DIR/domain.key

# Reiniciar el contenedor de HAProxy para que tome los nuevos certificados
sudo docker restart haproxy

echo "Certificado SSL generado y configurado correctamente."