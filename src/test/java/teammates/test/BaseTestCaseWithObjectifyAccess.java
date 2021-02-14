package teammates.test;

import com.google.cloud.datastore.DatastoreOptions;
import com.googlecode.objectify.ObjectifyFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.util.Closeable;

import teammates.storage.api.OfyHelper;

/**
 * Base class for all test cases which require access to the Objectify service. Requires a minimal GAE API environment
 * registered for creation of Datastore Key objects used in defining parent-child relationships in entities.
 */
public abstract class BaseTestCaseWithObjectifyAccess extends BaseTestCaseWithMinimalGaeEnvironment {
    private Closeable closeable;

    @BeforeClass
    public void setupObjectify() {
        ObjectifyService.init(new ObjectifyFactory(
                DatastoreOptions.newBuilder()
                        .setProjectId("tm-obj-v6-test")
                        .build()
                        .getService()
        ));
        OfyHelper.registerEntityClasses();
        closeable = ObjectifyService.begin();
    }

    @AfterClass
    public void tearDownObjectify() {
        closeable.close();
    }

}
