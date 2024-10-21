package de.hbrs.ia.code;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import de.hbrs.ia.model.SalesMan;
import de.hbrs.ia.model.SocialPerformanceRecord;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

public class ManagePersonalImpl implements IManagePersonal{
    private final MongoDatabase database;
    private final MongoCollection<Document> salesManCollection;
    private final MongoCollection<Document> performanceRecordCollection;

    public ManagePersonalImpl(MongoClient mongoClient, String dbName) {
        this.database = mongoClient.getDatabase(dbName);
        this.salesManCollection = database.getCollection("salesmen");
        this.performanceRecordCollection = database.getCollection("social_performance_records");
    }

    @Override
    public void createSalesMan(SalesMan record) {
        Document salesManDoc = record.toDocument();
        salesManCollection.insertOne(salesManDoc);
    }

    @Override
    public void addSocialPerformanceRecord(SocialPerformanceRecord record, SalesMan salesMan) {
        if (salesManExists(salesMan.getId())) {
            Document recordDoc = record.toDocument();
            recordDoc.append("salesManId", salesMan.getId());
            performanceRecordCollection.insertOne(recordDoc);
            //should the SocialPerformanceRecord java object also reference the salesman now??
        } else {
            throw new IllegalArgumentException("SalesMan with ID " + salesMan.getId() + " does not exist.");
        }
    }
    private boolean salesManExists(int sid) {
        return salesManCollection.find(new Document("sid", sid)).first() != null;
    }

    @Override
    public SalesMan readSalesMan(int sid) {
        Document doc = salesManCollection.find(new Document("sid", sid)).first();
        if (doc != null) {
            // Create a new SalesMan instance from the retrieved document
            // is this wanted?? or should it just get printed?
            return new SalesMan(doc.getString("firstname"), doc.getString("lastname"), sid);
        }
        return null;
    }

    @Override
    public List<SalesMan> readAllSalesMen() {
        List<SalesMan> salesMen = new ArrayList<>();
        for (Document doc : salesManCollection.find()) {
            SalesMan salesMan = new SalesMan(doc.getString("firstname"), doc.getString("lastname"), doc.getInteger("sid"));
            salesMen.add(salesMan);
        }
        return salesMen;
    }

    @Override
    public List<SocialPerformanceRecord> readSocialPerformanceRecord(SalesMan salesMan) {
        List<SocialPerformanceRecord> records = new ArrayList<>();

        // Find all documents for the SalesMan and map them to SocialPerformanceRecord
        for (Document doc : performanceRecordCollection.find(new Document("salesManId", salesMan.getId()))) {
            SocialPerformanceRecord record = createSocialPerformanceRecordFromDocument(doc, salesMan);
            records.add(record);
        }

        return records;
    }

    @Override
    public SocialPerformanceRecord readSocialPerformanceRecordByYear(SalesMan salesMan, int year) {
        Document doc = performanceRecordCollection.find(and(eq("salesManId", salesMan.getId()), eq("year", year))).first();
        // If document is found, map it to SocialPerformanceRecord
        if (doc != null) {
            return createSocialPerformanceRecordFromDocument(doc, salesMan);
        }
        return null;
    }

    // Helper method to create a SocialPerformanceRecord from a document
    private SocialPerformanceRecord createSocialPerformanceRecordFromDocument(Document doc, SalesMan salesMan) {
        SocialPerformanceRecord record = new SocialPerformanceRecord(doc.getInteger("year"));
        record.setSalesMan(salesMan); // Associate SalesMan with the record
        // Extract competences from the document and add them to the record
        List<Document> competencesList = doc.getList("competences", Document.class);
        if (competencesList != null) {
            for (Document competenceDoc : competencesList) {
                SocialPerformanceRecord.SocialCompetence competence = mapDocumentToCompetence(competenceDoc);
                record.getCompetences().add(competence);
            }
        }

        return record;
    }

    // Helper method to map a Document to a SocialCompetence object
    private SocialPerformanceRecord.SocialCompetence mapDocumentToCompetence(Document competenceDoc) {
        SocialPerformanceRecord.SocialCompetence competence = new SocialPerformanceRecord.SocialCompetence(
                competenceDoc.getInteger("id"),
                competenceDoc.getString("name")
        );
        competence.setTargetValue(competenceDoc.getInteger("targetValue"));
        competence.setActualValue(competenceDoc.getInteger("actualValue"));
        competence.setBonus(competenceDoc.getDouble("bonus"));
        competence.setComment(competenceDoc.getString("comment"));

        return competence;
    }

    @Override
    public void deleteSalesMan(int sid) {
        // Delete the SalesMan from the salesManCollection
        salesManCollection.deleteOne(eq("sid", sid));
        // Also delete associated performance records
        performanceRecordCollection.deleteMany(eq("salesManId", sid));
    }

    @Override
    public void deletePerformanceRecord(int sid, int year) {
        // Delete a specific performance record by SalesMan's ID and year
        performanceRecordCollection.deleteOne(and(eq("salesManId", sid), eq("year", year)));
    }
}
