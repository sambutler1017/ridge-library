module com.ridge {
    requires rxjava;
    requires transitive jackson.databind;
    requires jackson.core;
    requires jackson.annotations;
    requires jackson.datatype.jsr310;
    requires org.slf4j;
    requires org.slf4j.simple;
    requires transitive spring.messaging;
    requires spring.core;
    requires spring.beans;
    requires spring.web;
    requires spring.context;
    requires spring.websocket;
    requires transitive java.net.http;
    requires java.sql;

    exports com.ridge.api;
    exports com.ridge.socket;
    exports com.ridge.mapper;
    // exports com.ridge.test;
    // exports com.ridge.test.domain;
}
