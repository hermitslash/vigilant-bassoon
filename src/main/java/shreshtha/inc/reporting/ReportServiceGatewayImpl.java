package shreshtha.inc.reporting;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Repository;
import shreshtha.inc.common.ReportData;
import shreshtha.inc.common.ReportInfoData;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Repository
@RequiredArgsConstructor
public class ReportServiceGatewayImpl implements ReportServiceGateway {
    private final JpaReportInfoDataRepository jpaReportInfoDataRepository;
    @Override
    @SneakyThrows
    public JpaReportInfoData initiateReportData(ReportInfoData rd) {
        var jpaReportData = new JpaReportInfoData();
        jpaReportData.setDateCreated(OffsetDateTime.now(Clock.systemUTC()));
        jpaReportData.setInvoiceNo(rd.invoiceNo());
        jpaReportData.setReportData(rd.reportData());
        jpaReportData.setLastUpdated(OffsetDateTime.now(Clock.systemUTC()));
        return jpaReportInfoDataRepository.save(jpaReportData);
    }

    @Override
    public Stream<JpaReportInfoData> findAllReportInfoData(String customerId) {
        return jpaReportInfoDataRepository.findAll().stream().filter(jpaReportInfoData -> customerId.equals(jpaReportInfoData.getReportData().getCustomerId()));
    }

    @Override
    public Optional<JpaReportInfoData> findReportInfoDataByInvoiceNo(String invoiceNo) {
        return jpaReportInfoDataRepository.findById(invoiceNo);
    }

    @Override
    public Stream<JpaReportInfoData> findAllByCurrentDate(String currentDate, String customerId) {
        return jpaReportInfoDataRepository.findAll().stream().filter(rid -> (rid.getReportData().getCurrentDate().equals(currentDate) && (rid.getReportData().getCustomerId().equals(customerId))));
    }

	@Override
	public void updateReportDataDownloads(ReportInfoData reportInfoData) {
		reportInfoData.reportData().updateReportDataDownloads();
		var jpaReportInfoData = new JpaReportInfoData();
		jpaReportInfoData.setInvoiceNo(reportInfoData.invoiceNo());
		jpaReportInfoData.setReportData(reportInfoData.reportData());
		jpaReportInfoData.setDateCreated(reportInfoData.dateCreated());
		jpaReportInfoData.setLastUpdated(OffsetDateTime.now(Clock.systemUTC()));
		jpaReportInfoDataRepository.save(jpaReportInfoData);
	}
}
