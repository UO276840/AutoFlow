#!/bin/bash
if [ -e /var/run/docker.sock ]; then
    chown root:docker /var/run/docker.sock
    chmod 660 /var/run/docker.sock
fi

# Ejecutar el comando principal (por ejemplo, Jenkins)
exec "$@"
