package be.ucll.se.groep26backend.car.bdd;

import org.junit.runner.RunWith;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import io.cucumber.spring.SpringFactory;

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/resources/features",
        glue = "be.ucll.se.groep26backend.car.bdd.steps",
        plugin = {"pretty", "json:target/cucumber-report/cucumber.json", "html:target/cucumber-report/cucumber.html"},
        objectFactory = SpringFactory.class)
        
public class CucumberTest {
    
}
