package de.hbrs.ia.code;
import de.hbrs.ia.model.SalesMan;
import de.hbrs.ia.model.SocialPerformanceRecord;

import java.util.List;

/**
 * Code lines are commented for suppressing compile errors.
 * Are there any CRUD-operations missing?
 */
public interface IManagePersonal {
     public void createSalesMan( SalesMan record );

     public void addSocialPerformanceRecord(SocialPerformanceRecord record , SalesMan salesMan );
     //Remark: an SocialPerformanceRecord corresponds to part B of a bonus sheet

     public SalesMan readSalesMan( int sid );

     public List<SalesMan> readAllSalesMen();

     public List<SocialPerformanceRecord> readSocialPerformanceRecord( SalesMan salesMan );

     public SocialPerformanceRecord readSocialPerformanceRecordByYear(SalesMan salesMan, int year);

     public void deleteSalesMan(int sid);

     public void deletePerformanceRecord(int sid, int year);
}
