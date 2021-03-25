package teammates.storage.search;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.ConcurrentUpdateSolrClient;
import org.apache.solr.common.SolrInputDocument;
import teammates.common.datatransfer.InstructorSearchResultBundle;
import teammates.common.datatransfer.StudentSearchResultBundle;
import teammates.common.datatransfer.attributes.InstructorAttributes;
import teammates.common.datatransfer.attributes.StudentAttributes;
import teammates.common.exception.SearchNotImplementedException;
import teammates.common.exception.TeammatesException;
import teammates.common.util.Logger;
import teammates.common.util.StringHelper;

/**
 * Acts as a proxy to search service.
 */
public final class SearchManager {

    private static final String ERROR_PUT_DOCUMENT =
            "Failed to put document(s) %s into Solr. Root cause: %s ";
    private static final String ERROR_SEARCH_NOT_IMPLEMENTED =
            "Search service is not implemented";
    private static final String STUDENT_COLLECTION_NAME = "students";
    private static final String INSTRUCTOR_COLLECTION_NAME = "instructors";
    private static final Logger log = Logger.getLogger();

    private String searchServiceHost;

    public SearchManager(String searchServiceHost) {
        this.searchServiceHost = searchServiceHost;
    }

    private boolean isSearchServiceActive() {
        return !StringHelper.isEmpty(searchServiceHost);
    }

    /**
     * Searches for students.
     *
     * @param instructors the constraint that restricts the search result
     */
    public StudentSearchResultBundle searchStudents(String queryString, List<InstructorAttributes> instructors)
            throws SearchNotImplementedException {
        if (!isSearchServiceActive()) {
            throw new SearchNotImplementedException();
        }
        // TODO
        throw new SearchNotImplementedException();
    }

    /**
     * Batch creates or updates search documents for the given students.
     */
    public void putStudentSearchDocuments(StudentAttributes... students) {
        if (!isSearchServiceActive()) {
            log.severe(ERROR_SEARCH_NOT_IMPLEMENTED);
            return;
        }

        SolrClient client = getSolrClient();

        List<SolrInputDocument> studentDocs = new ArrayList<>();

        for (StudentAttributes student : students) {
            SolrInputDocument studentDoc = new SolrInputDocument();

            studentDoc.addField("name", student.getName());
            studentDoc.addField("email", student.getEmail());
            studentDoc.addField("course_id", student.getCourse());
            // TODO: add field for course name
            studentDoc.addField("team", student.getTeam());
            studentDoc.addField("section", student.getSection());

            studentDocs.add(studentDoc);
        }

        addDocumentsToCollection(client, studentDocs, STUDENT_COLLECTION_NAME);
    }

    private ConcurrentUpdateSolrClient getSolrClient() {
        return new ConcurrentUpdateSolrClient.Builder(searchServiceHost).build();
    }

    /**
     * Removes student search documents based on the given keys.
     */
    public void deleteStudentSearchDocuments(String... keys) {
        if (!isSearchServiceActive()) {
            log.severe(ERROR_SEARCH_NOT_IMPLEMENTED);
            return;
        }
        // TODO
    }

    /**
     * Searches for instructors.
     */
    public InstructorSearchResultBundle searchInstructors(String queryString) throws SearchNotImplementedException {
        if (!isSearchServiceActive()) {
            throw new SearchNotImplementedException();
        }
        // TODO
        throw new SearchNotImplementedException();
    }

    /**
     * Batch creates or updates search documents for the given instructors.
     */
    public void putInstructorSearchDocuments(InstructorAttributes... instructors) {
        if (!isSearchServiceActive()) {
            log.severe(ERROR_SEARCH_NOT_IMPLEMENTED);
            return;
        }

        SolrClient client = getSolrClient();

        List<SolrInputDocument> instructorDocs = new ArrayList<>();

        for (InstructorAttributes instructor : instructors) {
            SolrInputDocument instructorDoc = new SolrInputDocument();

            instructorDoc.addField("name", instructor.getName());
            instructorDoc.addField("email", instructor.getEmail());
            instructorDoc.addField("course_id", instructor.getCourseId());
            // TODO: add field for course name
            instructorDoc.addField("google_id", instructor.getGoogleId());
            instructorDoc.addField("role", instructor.getRole());
            instructorDoc.addField("displayed_name", instructor.getDisplayedName());

            instructorDocs.add(instructorDoc);
        }

        addDocumentsToCollection(client, instructorDocs, INSTRUCTOR_COLLECTION_NAME);
    }

    private void addDocumentsToCollection(SolrClient client, List<SolrInputDocument> docs,
                                          String collectionName) {
        try {
            client.add(collectionName, docs);
            client.commit(collectionName);
        } catch (SolrServerException e) {
            log.severe(String.format(ERROR_PUT_DOCUMENT, docs, e.getRootCause())
                    + TeammatesException.toStringWithStackTrace(e));
        } catch (IOException e) {
            log.severe(String.format(ERROR_PUT_DOCUMENT, docs, e.getCause())
                    + TeammatesException.toStringWithStackTrace(e));
        }
    }

    /**
     * Removes instructor search documents based on the given keys.
     */
    public void deleteInstructorSearchDocuments(String... keys) {
        if (!isSearchServiceActive()) {
            log.severe(ERROR_SEARCH_NOT_IMPLEMENTED);
            return;
        }
        // TODO
    }

}
