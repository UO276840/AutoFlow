# Etapa de construcción
FROM mcr.microsoft.com/dotnet/sdk:8.0 AS build
WORKDIR /src

# Copia los archivos de proyecto y restaura las dependencias
COPY *.sln .
COPY ["TiendaOnline/TiendaOnline.csproj", "TiendaOnline/"]
COPY ["TestTiendaOnline/TestTiendaOnline.csproj", "TestTiendaOnline/"]
RUN dotnet restore "TiendaOnline/TiendaOnline.csproj"

# Copia el resto de los archivos y compila la aplicación
COPY . .
WORKDIR "/src/TiendaOnline"
RUN dotnet publish -c Release -o /app/out

# Etapa de runtime
FROM mcr.microsoft.com/dotnet/aspnet:8.0 AS runtime
WORKDIR /app
COPY --from=build /app/out .

ENV ASPNETCORE_URLS="http://0.0.0.0:7000"

# Comando para ejecutar la aplicación
ENTRYPOINT ["dotnet", "TiendaOnline.dll"]
