# EVInventory

This program graps inventory information from various EV manufacturers. Currently only Tesla is supported. And it writes inventory number and prices of different models at different places in a remote Time Series DB (TSDB), TT(TickTock). 

There is a grafana server providing panels (including maps).

compile:
[Yi-MBP EVInventory (main)]$ mvn clean package assembly:single

run:
[Yi-MBP EVInventory (main)]$ java -cp target/evinventory-0.1-jar-with-dependencies.jar com.tt.Main 127.0.0.1 6182
