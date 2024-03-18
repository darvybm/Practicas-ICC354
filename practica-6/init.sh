#!/bin/bash

echo "Configuración básica de HAProxy"

# Eliminar todas las aplicaciones Java.
pkill java

# Crear una copia del archivo de configuración actual.
if [ ! -e "/etc/haproxy/haproxy.cfg.original" ]; then
  echo "Creando archivo de backup"
  sudo cp /etc/haproxy/haproxy.cfg /etc/haproxy/haproxy.cfg.original
fi

# Copiar el archivo de configuración de HAProxy.
sudo cp haproxy.cfg /etc/haproxy/haproxy.cfg

# Instalando docker y docker-compose
sudo apt update
sudo apt install -y docker.io docker-compose

# Instalando certbot y generando certificados SSL
sudo apt-get remove certbot
sudo apt-get update
sudo apt-get install -y software-properties-common
sudo add-apt-repository -y ppa:certbot/certbot
sudo apt-get update
sudo apt-get install -y certbot

# Dominio para el que se generará el certificado SSL
DOMAIN="pract6.turnos.do"

# Directorio donde se guardarán los archivos generados por Let's Encrypt
LE_DIR="/etc/letsencrypt/live/$DOMAIN"

# Directorio donde se guardará el certificado y la clave privada
CERT_DIR="/etc/ssl/certs"

# Comando para obtener el certificado SSL utilizando certbot de Let's Encrypt
sudo certbot certonly --standalone --preferred-challenges http -d $DOMAIN

sudo mkdir -p "$CERT_DIR"
cat "$LE_DIR/fullchain.pem" "$LE_DIR/privkey.pem" > "$CERT_DIR/$DOMAIN.pem"

sudo chmod -R go-rwx "$CERT_DIR"

# Reiniciar el contenedor de HAProxy para que tome los nuevos certificados
sudo systemctl restart haproxy

echo "Certificado SSL generado y configurado correctamente."