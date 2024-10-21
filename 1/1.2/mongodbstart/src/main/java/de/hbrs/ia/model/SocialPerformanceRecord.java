package de.hbrs.ia.model;

import org.bson.Document;

import java.util.*;

public class SocialPerformanceRecord {
    private SalesMan salesMan;
    private Set<SocialCompetence> competences;
    private int year;

    private static final List<SocialCompetence> PREDEFINED_COMPETENCES = Arrays.asList(
            new SocialCompetence(1, "Leadership Competence"),
            new SocialCompetence(2, "Openness to Employee"),
            new SocialCompetence(3, "Social Behaviour to Employee"),
            new SocialCompetence(4, "Attitude towards Client"),
            new SocialCompetence(5, "Communication Skills"),
            new SocialCompetence(6, "Integrity to Company")
    );

    public SocialPerformanceRecord(int year) {
        this.year = year;
        this.competences = new HashSet<>(PREDEFINED_COMPETENCES);
    }

    public SalesMan getSalesMan() {
        return salesMan;
    }

    public void setSalesMan(SalesMan salesMan) {
        this.salesMan = salesMan;
    }

    public Set<SocialCompetence> getCompetences() {
        return Collections.unmodifiableSet(competences);
    }

    public int getYear() {
        return year;
    }

    public double getAverageTargetValue() {
        if (competences.isEmpty()) {
            return 0;
        }
        int totalTargetValue = 0;
        for (SocialCompetence competence : competences) {
            totalTargetValue += competence.getTargetValue();
        }
        return (double) totalTargetValue / competences.size();
    }

    public double getAverageActualValue() {
        int totalActualValue = 0;
        for (SocialCompetence competence : competences) {
            totalActualValue += competence.getActualValue();
        }
        if (competences.isEmpty()) {
            return 0;
        }
        return (double) totalActualValue / competences.size();
    }

    public double getTotalBonus() {
        double totalBonus = 0;
        for (SocialCompetence competence : competences) {
            totalBonus += competence.getBonus();
        }
        return totalBonus;
    }

    public Document toDocument() {
        Document document = new Document();

        // Create a list of documents for competences
        List<Document> competencesList = new ArrayList<>();
        for (SocialCompetence competence : competences) {
            competencesList.add(competence.toDocument());
        }

        // Append the competences to the document
        document.append("competences", competencesList);

        // Append additional information (like averages and total bonus)
        document.append("averageTargetValue", getAverageTargetValue());
        document.append("averageActualValue", getAverageActualValue());
        document.append("totalBonus", getTotalBonus());

        return document;
    }

    public static class SocialCompetence {
        private final int id;
        private String name;
        private int targetValue;
        private int actualValue;
        private double bonus;
        private String comment;

        public SocialCompetence(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getTargetValue() {
            return targetValue;
        }

        public void setTargetValue(int targetValue) {
            this.targetValue = targetValue;
        }

        public int getActualValue() {
            return actualValue;
        }

        public void setActualValue(int actualValue) {
            this.actualValue = actualValue;
        }

        public double getBonus() {
            return bonus;
        }

        public void setBonus(double bonus) {
            this.bonus = bonus;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public Document toDocument() {
            Document document = new Document()
                    .append("id", id)
                    .append("name", name)
                    .append("targetValue", targetValue)
                    .append("actualValue", actualValue)
                    .append("bonus", bonus);
            if (comment != null) {
                document.append("comment", comment);
            }
            return document;
        }

    }
}
