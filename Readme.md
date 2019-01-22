## Installation

```
git clone https://github.com/karussell/testbug
cd testbug
mvn clean install
```

## Fetch Data

```
wget https://planet.openstreetmap.org/pbf/planet-latest.osm.pbf
# TODO: how to wget cgiar data?
```

## Run

```
cd testbug
/path/jdk-11.0.1/bin/java -Dgraphhopper.datareader.file=planet-latest.osm.pbf -Dgraphhopper.graph.location=tmp-gh -Xmx230g -Xms230g -jar target/testbug-1.0-SNAPSHOT.jar
```