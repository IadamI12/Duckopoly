# Duckopoly

Egy Java és Swing alapú társasjáték Monopoly-szerű játékmenettel. A játék 2-4
játékos számára készült, és támogatja az új játék indítását, valamint a mentett
játékállás betöltését.

## Követelmények

- Java Development Kit (JDK) 25 vagy újabb
- Apache Maven 3.9 vagy újabb
- Grafikus asztali környezet, mert a program Swing felületet használ

A projekt Java 25-re van beállítva a [hf/pom.xml](hf/pom.xml) fájlban.

## Projektstruktúra

```text
Duckopoly/
├── hf/
│   ├── pom.xml
│   └── src/
│       ├── main/
│       │   ├── java/          # A játék forráskódja
│       │   └── resources/     # Pálya-, bónusz- és képfájlok
│       └── test/              # JUnit tesztek
├── doc/                       # Generált JavaDoc dokumentáció
└── README.md
```

## Klónozás

```bash
git clone https://github.com/IadamI12/Duckopoly.git
cd Duckopoly
```

## Futtatás Maven segítségével

A parancsokat a repository gyökeréből, vagyis abból a mappából kell futtatni,
amelyben a `hf` könyvtár található. A játék jelenlegi erőforrás-kezelése
relatív útvonalakat használ, ezért fontos a megfelelő munkakönyvtár.

### Fordítás

```bash
mvn -f hf/pom.xml clean compile
```

### Játék indítása Windows alatt

PowerShellben:

```powershell
java -cp "hf\target\classes" com.example.controller.Main
```

Parancssorban ugyanaz a parancs használható:

```bat
java -cp "hf\target\classes" com.example.controller.Main
```

### Játék indítása Linux vagy macOS alatt

```bash
java -cp "hf/target/classes" com.example.controller.Main
```

Indítás után a program grafikus ablakban felajánlja új játék indítását vagy
korábbi mentés betöltését.

## Tesztek futtatása

Az összes JUnit teszt futtatása:

```bash
mvn -f hf/pom.xml test
```

A tesztjelentések a `hf/target/surefire-reports/` könyvtárban jelennek meg.

## JavaDoc megtekintése

A repository tartalmaz előre generált dokumentációt a `doc/` könyvtárban.
Megnyitható például a [doc/index.html](doc/index.html) fájl böngészőben.

Új JavaDoc generálása Mavenből:

```bash
mvn -f hf/pom.xml javadoc:javadoc
```

## Gyakori problémák

### `java is not recognized` vagy `java: command not found`

Ellenőrizd, hogy a JDK telepítve van-e, majd add hozzá a JDK `bin` könyvtárát
a `PATH` környezeti változóhoz. Ezután nyiss új terminált, és ellenőrizd:

```bash
java -version
mvn -version
```

### A Maven nem tud Java 25-tel fordítani

Ellenőrizd, hogy valóban JDK 25 fut-e:

```bash
java -version
```

A Maven fordító a `hf/pom.xml` alapján Java 25-ös forrás- és célverziót használ,
ezért régebbi JDK-val a fordítás sikertelen lehet.

### Az alkalmazás nem találja a képeket vagy az adatfájlokat

Az indítóparancsot a repository gyökeréből futtasd, ne a `hf` könyvtárból.
Ellenőrizd, hogy ezek a fájlok léteznek:

```text
hf/src/main/resources/tiles.txt
hf/src/main/resources/bonustiles.txt
hf/src/main/resources/duck.png
```
