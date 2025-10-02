package com.techfoot.stockspree;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.techfoot.stockspree.Business.DataContracts.CreateProducts.CreateProductsOutput_IP;
import com.techfoot.stockspree.InboundAdaptors.REST.Database.Input_CreateDbIA;
import com.techfoot.stockspree.InboundAdaptors.REST.Database.Output_CreateDbIA;
import com.techfoot.stockspree.InboundPort.CreateDatabase.C_CreateDatabaseHandler;
import com.techfoot.stockspree.InboundPort.Product.C_CreateProductsHandler;
import com.techfoot.stockspree.InboundAdaptors.GlobalExceptionHandling;

import jakarta.validation.ConstraintViolationException;

@SpringBootTest
@Validated
@ActiveProfiles("mock")
public class C_CreateProductInboundPortTest {
    @Autowired
    private C_CreateDatabaseHandler createDatabaseHandler;

    @Autowired
    private C_CreateProductsHandler createProductHandler;

    @Autowired
    private GlobalExceptionHandling globalExceptionHandling;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private ObjectMapper mapper = new ObjectMapper();
    private CreateProductTestData testData;
    private static String dbName;
    private static Integer count = 0;
    private Input_CreateDbIA testDB;

    static private boolean setUpIsDone = false;
    public void createTestDB () {
        if (setUpIsDone) {
            return;
        }
        Resource resource = new ClassPathResource("Data/createTestDB.json");
        try {
            testDB = mapper.readValue(resource.getFile(), Input_CreateDbIA.class);
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
            e.printStackTrace();
            if (e instanceof StreamReadException) {
                System.err.println("This was a StreamReadException.");
            } else if (e instanceof DatabindException) {
                System.err.println("This was a DatabindException.");
            }
        }
        dbName = testDB.getDBName();
        String dropDB = "DROP DATABASE IF EXISTS " + dbName;
        jdbcTemplate.execute(dropDB);
        Output_CreateDbIA createDatabaseResponse = createDatabaseHandler.createDb(testDB);
        setUpIsDone = true;
    }


    @BeforeEach
    public void setUp() throws IOException {
        System.out.println("setUp");
        createTestDB();
        mapper.registerModule(new JavaTimeModule());
        Resource resource = new ClassPathResource("Data/createProject" + ++count + ".json");
        testData = mapper.readValue(resource.getFile(), CreateProductTestData.class);
        System.out.println("TestData contents: " + testData);
    }

    @RepeatedTest(30)
    public void test() {
        String sql = "USE " + dbName + ";";
        jdbcTemplate.execute(sql);
        try {
            CreateProductsOutput_IP actualResponse = createProductHandler.createProducts(testData.getProducts());
            CreateProductsOutput_IP expectedResponse = testData.getResponse();

            System.out.println("create project response: " + actualResponse);

            assertEquals(expectedResponse, actualResponse);
        } catch (ConstraintViolationException e) {
            globalExceptionHandling.handleConstraintViolationException(e);
        }

    }

}