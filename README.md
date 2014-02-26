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
        <resource-root path="mod_cluster-custom-metric-1.0-SNAPSHOT.jar"/>
    </resources>
    ```

  1. Copy the jar
  
    ```
        cp target/mod_cluster-custom-metric-1.0-SNAPSHOT.jar AS7_HOME/modules/system/layers/base/org/jboss/as/modcluster/main/
    ```
