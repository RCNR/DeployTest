package openapi.openapitest.domain.stock.service;

import lombok.RequiredArgsConstructor;
import openapi.openapitest.domain.stock.entity.NasdaqStockCode;
import openapi.openapitest.domain.stock.entity.StockCode;
import openapi.openapitest.domain.stock.repository.NasdaqCodeRepository;
import openapi.openapitest.domain.stock.repository.StockCodeRepository;
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
    private final NasdaqCodeRepository nasdaqCodeRepository;

    public void process() throws IOException {
        stockCodeRepository.deleteAll();

        String kospiZipUrl = "https://new.real.download.dws.co.kr/common/master/kospi_code.mst.zip";
        String kosdaqZipUrl = "https://new.real.download.dws.co.kr/common/master/kosdaq_code.mst.zip";
        String nasdaqZipUrl = "https://new.real.download.dws.co.kr/common/master/nasmst.cod.zip";

        String kospiZipPath = "C:\\Users\\cjsla\\Downloads\\stockcodes\\kospi_code.mst.zip";
        String kosdaqiZipPath = "C:\\Users\\cjsla\\Downloads\\stockcodes\\kosdaq_code.mst.zip";
        String nasdaqiZipPath = "C:\\Users\\cjsla\\Downloads\\stockcodes\\nasmst.cod.zip";

        String extractDir = "/stockcodes/";
        String kospiTxtFileName = "kospi_code.mst";
        String kosdaqTxtFileName = "kosdaq_code.mst";
        String nasdaqTxtFileName = "nasmst.cod";

        download(kospiZipUrl, kospiZipPath);
        unzip(kospiZipPath, extractDir);
        List<String> kospiStockCodes = extractStockCode(extractDir + kospiTxtFileName);
        saveStockCodes(kospiStockCodes);

        download(kosdaqZipUrl, kosdaqiZipPath);
        unzip(kosdaqiZipPath, extractDir);
        List<String> kosdaqStockCodes = extractStockCode(extractDir + kosdaqTxtFileName);
        saveStockCodes(kosdaqStockCodes);

        download(nasdaqZipUrl, nasdaqiZipPath);
        unzip(nasdaqiZipPath, extractDir);
        List<String> nasdaqStockCodes = extractNasdaqStockCode(extractDir + nasdaqTxtFileName);
        saveNasdaqStockCodes(nasdaqStockCodes);
    }

    public void saveNasdaqStockCodes(List<String> codes) {
        for (String code : codes) {
            nasdaqCodeRepository.save(new NasdaqStockCode(code));
        }
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
            if (line.startsWith("F")) continue;
            else if (line.startsWith("Q")) {
                String code = line.substring(0, 7).trim();
                codes.add(code);
            }
            else if (line.startsWith("J")) {
                String code = line.substring(1, 7).trim();
                codes.add(code);
            }
            else if (line.length() >= 6) {
                String code = line.substring(0, 6).trim();
                codes.add(code);
            }
        }
        br.close();
        return codes;
    }

    public List<String> extractNasdaqStockCode(String txtFilePath) throws IOException {

        List<String> codes = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(txtFilePath));

        String line;
        while ((line = br.readLine()) != null) {
            String[] tokens = line.split("\t");

            if (tokens.length >= 5) {
                codes.add(tokens[4]);
            }
        }
        br.close();
        return codes;
    }
}
