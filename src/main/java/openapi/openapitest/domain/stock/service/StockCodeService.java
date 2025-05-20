package openapi.openapitest.domain.stock.service;

import lombok.RequiredArgsConstructor;
import openapi.openapitest.domain.stock.entity.jpa.StockCode;
import openapi.openapitest.domain.stock.repository.jpa.StockCodeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Service
@RequiredArgsConstructor
@Transactional
public class StockCodeService {

    private final StockCodeRepository stockCodeRepository;

    public void process() throws IOException {
        stockCodeRepository.deleteAll();

        String zipUrl = "https://new.real.download.dws.co.kr/common/master/kospi_code.mst.zip";

        String zipPath = "C:\\Users\\cjsla\\Downloads\\stockcodes\\kospi_code.mst.zip";
        String extractDir = "/stockcodes/";
        String txtFileName = "kospi_code.mst";

        download(zipUrl, zipPath);
        unzip(zipPath, extractDir);
        List<String> stockCodes = extractStockCode(extractDir + txtFileName);
        saveStockCodes(stockCodes);
    }

    public void saveStockCodes(List<String> codes) {
        for (String code : codes) {
            stockCodeRepository.save(new StockCode(code));
        }
    }

    /**
     * zip 파일 다운로드
     * zip 압축 해제 후 TXT 파일 추출
     * 각 줄의 맨 앞 6자리 주식 코드 추출
     * DB에 저장
     */

    public void download(String urlInfo, String destFile) throws IOException {

        URL url = new URL(urlInfo);
        ReadableByteChannel rbc = Channels.newChannel(url.openStream());

        FileOutputStream fos = new FileOutputStream(destFile);
        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);

        fos.close();
        rbc.close();

    }

    public void unzip(String zipFilePath, String destDir) throws IOException {

        File dir = new File(destDir);

        if (!dir.exists()) dir.mkdir();
        byte[] buffer = new byte[1024];

        ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFilePath));
        ZipEntry zipEntry = zis.getNextEntry();

        while (zipEntry != null) {
            File newFile = new File(destDir, zipEntry.getName());
            FileOutputStream fos = new FileOutputStream(newFile);
            int len;

            while ((len = zis.read(buffer)) > 0) {
                fos.write(buffer, 0, len);
            }
            fos.close();
            zipEntry = zis.getNextEntry();
        }

        zis.closeEntry();
        zis.close();
    }

    public List<String> extractStockCode(String txtFilePath) throws IOException {

        List<String> codes = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(txtFilePath));

        String line;
        while ((line = br.readLine()) != null) {
            if (line.length() >= 6) {
                String code = line.substring(0, 6).trim();
                codes.add(code);
            }
        }
        br.close();
        return codes;
    }
}
