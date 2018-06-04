[![Build Status](https://travis-ci.org/sunlaud/uz-findticket.svg?branch=master)](https://travis-ci.org/sunlaud/uz-findticket)

# uz-findticket


## Build

```
mvn clean install
```

## Run

```
java -jar findticket-cli-app/target/findticket-cli-app-1.0-SNAPSHOT-jar-with-dependencies.jar
```


## Release
1. ```mvn --batch-mode release:prepare```
2. ```mvn  release:clean```
3. ```git push --follow-tags```


TODO:

* possible bug - Jackson fails to parse day of week which has ukrainian symbol <'> e.g. <п'ятниця>, reproduced only on real device