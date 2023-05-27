package shreshtha.inc.reporting;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcType;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.dialect.PostgreSQLJsonbJdbcType;
import shreshtha.inc.common.ReportData;

import java.time.OffsetDateTime;

@Entity
@Table(name = "report_info_data")
@Data
public class JpaReportInfoData {
    @Id
    @Column(nullable = false, updatable = false, length = 25)
    private String invoiceNo;

    @JdbcType(PostgreSQLJsonbJdbcType.class)
    @Column(columnDefinition = "jsonb")
    private ReportData reportData;

    @CreationTimestamp
    @Column(nullable = false)
    private OffsetDateTime dateCreated;

    @UpdateTimestamp
    @Column(nullable = false)
    private OffsetDateTime lastUpdated;
}