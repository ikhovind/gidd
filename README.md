# gidd
Vi har lagt opp prosjektet slik at vi har deployet backend til en AWS server, slik at alle kall fra frontend spørres fra denne. Frontend må derimot kjøres lokalt. Det er også mulig å kjøre backend lokalt.  

### Databastilgang
Hvis man ønsker å kjøre backend lokalt så er man avhengig av egen database, inloggingsinformasjonen til denne må man legge i en application.properties-fil. Den filen skal ligge her i mappestrukturen til prosjektet:
```
Backend/src/main/resources/
```
Og skal se slik ut:
```
RDSHOSTNAME=mysql-ait.stud.idi.ntnu.no
RDSPORT=3306
RDSDBNAME=databasenavn
RDSUSERNAME=brukernavn
RDSPASSWORD=passord
server.port=8080
secretKey = 4C8kum4LxyKWYLM78sKdXrzBjDCFyfX
```
Bytt ut rdshostname, rdsport, rdsdbname rdsusername og redspassword til egne verdier fra egen databaseløsning

SecretKey blir brukt til å generere tokens, naturligvis så burde vi ikke ha lagt den ut for sikkerheten sin skyld, men for at prosjektet skal være lettest mulig å klone så har vi valgt å dele den likevel. 

Alternativ 1 (Kjøre frontend og backend lokalt i Docker) :
---
1. Clone eller laste ned koden til din egen datamaskin 
2. Laste ned Docker. Vi anbefaler å laste ned Docker-Desktop som du kan laste ned her: **https://www.docker.com/products/docker-desktop**
3. Starte opp docker-desktop
4. Gå til filen Axios.tsx i kildekoden som du finner under "./client/src" og endre der det nå står http://13.51.58.86:8080 på linje 5 til http://localhost:8080
5. Deretter skriv docker-compose up i når du befinner deg i roten av prosjektet i terminalen, og vent til programmet kjører
6. Du vil nå kunne åpne applikasjonen i nettleseren på http://localhost:3000

Alternativ 2 (Kjøre frontend lokalt med Node med deployet backend) :
---
1. Clone eller laste ned koden til din egen datamaskin 
2. Last ned Node, som kan gjøres her: https://nodejs.org/en/
3. Når det er lasted ned så beveg deg inn i mappen client i terminalen
4. Kjør "npm install"
5. Kjør "npm start" 
6. Du vil nå kunne finne applikasjonen på http://localhost:3000 i nettleseren med mindre det står noe annet i terminalen 

Alternativ 3 (Kjøre frontend lokalt i Docker med deployet backend) :
---
1. Clone eller laste ned koden til din egen datamaskin 
2. Laste ned Docker. Vi anbefaler å laste ned Docker-Desktop som du kan laste ned her: **https://www.docker.com/products/docker-desktop**
3. Start opp docker-desktop
4. Beveg deg inn i mappen client i terminalen
5. Kjør deretter "docker build -t 'DITT_IMAGE_NAVN' ." (eks: docker build -t GIDD .)
6. For å kjøre skriver du i chatroulette-mappen 'docker run -p 3000:3000 'DITT_IMAGE_NAVN' (eks: docker run -p 3000:3000 GIDD)
7. Du vil nå kunne finne applikasjonen på http://localhost:3000 i nettleseren med mindre det står noe annet i terminalen

Alternativ 4 (Kjøring av backend med Maven og valgfri kjøring av frontend):  
---
1. Clone eller laste ned koden til din egen datamaskin 
2. Last ned Maven, dette kan gjøres her: https://maven.apache.org/
3. Skriv maven --version i kommandolinjen og sjekk om du Java version er 11 eller mere. Dette er nødvendig for at dette skal fungere
4. Beveg deg deretter inn i mappen Backend i terminalen 
5. Kjør mvn spring-boot:run
6. Serveren vil nå starte på http://localhost:8080.
7. Gå til filen Axios.tsx i kildekoden som du finner under "./client/src" og endre der det nå står http://13.51.58.86:8080 på linje 5 til http://localhost:8080
8. Start frontend

Alternativ 5 (Kjøring av backend i Docker og valgfri kjøring av frontend):  
---
1. Clone eller laste ned koden til din egen datamaskin 
2. Laste ned Docker. Vi anbefaler å laste ned Docker-Desktop som du kan laste ned her: **https://www.docker.com/products/docker-desktop**
3. Start opp docker-desktop
4. Beveg deg inn i mappen backend i terminalen
5. Kjør deretter "docker build -t 'DITT_IMAGE_NAVN' ." (eks: docker build -t GIDD .)
6. For å kjøre skriver du Backend-mappen 'docker run -p 8080:8080 'DITT_IMAGE_NAVN' (eks: docker run -p 8080:8080 GIDD)
7. Gå til filen Axios.tsx i kildekoden som du finner under "./client/src" og endre der det nå står http://13.51.58.86:8080 på linje 5 til http://localhost:8080
8. Start frontend
