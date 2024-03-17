#!/usr/bin/env bash
echo "Configuración básica de HAProxy"

# Eliminando todas las aplicaciones Java.
killall java

# Creando una copia del archivo actual.
if [ ! -e "/etc/haproxy/haproxy.cfg.original" ]; then
  echo "Creando archivo de backup"
  sudo cp /etc/haproxy/haproxy.cfg /etc/haproxy/haproxy.cfg.original
fi

# Copiando el archivo de configuración de HAProxy.
sudo cp Practicas-ICC354/practica-6/haproxy.cfg /etc/haproxy/haproxy.cfg

# Instalando docker
sudo apt install docker-compose

# Instalando certificados SSL
sudo apt-get remove certbot
sudo apt-get update
sudo apt-get install software-properties-common
sudo add-apt-repository ppa:certbot/certbot
sudo apt-get update
sudo apt-get install certbot

# Dominio para el que se generará el certificado SSL
DOMAIN="p6.turnos.do"

# Directorio donde se guardarán los archivos generados por Let's Encrypt
LE_DIR="/etc/letsencrypt"

# Directorio donde se guardará el certificado y la clave privada
CERT_DIR="/etc/ssl/certs"

# Comando para obtener el certificado SSL utilizando certbot de Let's Encrypt
certbot certonly --standalone --preferred-challenges http -d $DOMAIN

# Copiar el certificado y la clave privada al directorio de certificados
cp $LE_DIR/live/$DOMAIN/fullchain.pem $CERT_DIR/domain.pem
cp $LE_DIR/live/$DOMAIN/privkey.pem $CERT_DIR/domain.key

# Cambiar los permisos de los archivos de certificado y clave privada
chmod 644 $CERT_DIR/domain.pem
chmod 600 $CERT_DIR/domain.key

# Reiniciar el contenedor de HAProxy para que tome los nuevos certificados
docker restart haproxy

echo "Certificado SSL generado y configurado correctamente."