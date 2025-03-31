import docker
import time
import os

def check_container_connectivity(client, source, targets):
    try:
        source_container = client.containers.get(source)
        for target in targets:
            exit_code, output = source_container.exec_run(f'ping -c 4 {target}')
            if exit_code == 0:
                print(f"Comunicación exitosa entre {source} y {target}")
            else:
                print(f"Error en comunicación entre {source} y {target}")
    except Exception as e:
        print(f"Error verificando conectividad: {e}")

def check_container_status(client):
    try:
        containers = client.containers.list(all=True)
        inactive_containers = []
        for container in containers:
            if container.status != 'running':
                inactive_containers.append(container.name)
        if inactive_containers:
            print("Contenedores inactivos detectados:", ", ".join(inactive_containers))
        else:
            print("Todos los contenedores están activos.")
    except Exception as e:
        print(f"Error al obtener el estado de los contenedores: {e}")

def restart_container(client, container_name):
    try:
        container = client.containers.get(container_name)
        container.restart()
        time.sleep(5)  # Espera a que reinicie completamente
        print(f"{container_name} reiniciado exitosamente")
    except Exception as e:
        print(f"No se pudo reiniciar {container_name}: {e}")

if __name__ == "__main__":
    os.system('cls')  # Limpia la terminal en Windows
    client = docker.from_env()

    # Caso de Prueba 1: Validación de comunicación entre contenedores
    print("\nCaso de Prueba 1: Validación de comunicación entre contenedores")
    check_container_connectivity(client, "jenkins", ["sonarqube", "agent", "dockercontainers-selenoid-1"])

    # Apagar SonarQube antes del segundo test
    print("\nApagando SonarQube para el segundo test...")
    try:
        container = client.containers.get("sonarqube")
        container.stop()
        print("SonarQube apagado exitosamente.")
    except Exception as e:
        print(f"Error al apagar SonarQube: {e}")

    # Caso de Prueba 2: Detección de contenedores caídos
    print("\nCaso de Prueba 2: Detección de contenedores caídos")
    check_container_status(client)

    # Caso de Prueba 3: Recuperación de un contenedor caído
    print("\nCaso de Prueba 3: Recuperación de un contenedor caído")
    restart_container(client, "sonarqube")
