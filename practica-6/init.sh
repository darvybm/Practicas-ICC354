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
LE_DIR="/etc/letsencrypt/live/$DOMAIN"

# Directorio donde se guardará el certificado y la clave privada
CERT_DIR="/etc/haproxy/certs"

# Comando para obtener el certificado SSL utilizando certbot de Let's Encrypt
sudo certbot certonly --standalone -d $DOMAIN -v

sudo mkdir -p $CERT_DIR
sudo -E bash -c "cat $LE_DIR/fullchain.pem $LE_DIR/privkey.pem >$CERT_DIR/$DOMAIN.pem"

sudo chmod -R go-rwx $CERT_DIR


# Reiniciar el contenedor de HAProxy para que tome los nuevos certificados
sudo service haproxy stop && sudo service haproxy start

echo "Certificado SSL generado y configurado correctamente."