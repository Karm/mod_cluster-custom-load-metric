mod_cluster Custom Load Metric
==============================

This load metric parses the load from a specified file with a specified regular expression (matching group 1).
To read about mod_cluster, load metrics and dynamically configured load balancing, visit [jboss.org/mod_cluster](http://www.jboss.org/mod_cluster).

Plug & Play
-----------

 1. Build 
    ```mvn package```

 1. Install
  1. mod_cluster subsystem in, e.g. ha profile or standalone-ha.xml file:

    ```
    <subsystem xmlns="urn:jboss:domain:modcluster:1.2">
        <mod-cluster-config advertise-socket="modcluster" connector="ajp">
            <dynamic-load-provider>
                <custom-load-metric class="biz.karms.modcluster.CustomLoadMetric">
                    <property name="loadfile" value="/tmp/myload" />
                    <property name="parseexpression" value="^LOAD: ([0-9]*)$" />
                </custom-load-metric>  
            </dynamic-load-provider>
    </mod-cluster-config>
    </subsystem>
    ```
  1. modules/system/layers/base/org/jboss/as/modcluster/main/module.xml:
  
    ```
    <resources>
        ...
        <resource-root path="mod_cluster-custom-metric.jar"/>
    </resources>
    ```

  1. Copy the jar
  
    ```
        cp target/mod_cluster-custom-metric.jar AS7_HOME/modules/system/layers/base/org/jboss/as/modcluster/main/
    ```

#### Fine tuning

One may play with ```capacity```, ```history``` and ```weight``` (in case one uses more metrics). Take a look at [The capacity of a metric](http://docs.jboss.org/mod_cluster/1.2.0/html_single/#java.load) in the documentation.

For instance, with this metric, if you have ```LOAD: 1``` in ```/tmp/myload``` and ```<property name="capacity" value="2" />```, you will get reported Load: 50 on mod_cluster manager console. If you set load in the file to 250 and change capacity to 1000, you will get Load: 75 on mod_cluster manager console. Bigger the number, up to 100, lower the actual load. These are special values:

 * load : load factor from the cluster manager.
 * load > 0  : a load factor.
 * load = 0  : standby worker.
 * load = -1 : errored worker.
 * load = -2 : just do a cping/cpong.       

