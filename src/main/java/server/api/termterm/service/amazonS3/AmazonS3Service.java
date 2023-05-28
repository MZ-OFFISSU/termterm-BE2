package server.api.termterm.service.amazonS3;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.api.termterm.domain.member.Member;
import server.api.termterm.response.BizException;
import server.api.termterm.response.amazonS3.AmazonS3ResponseType;

import java.net.URL;
import java.util.Date;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class AmazonS3Service {

    @Value("${cloud.aws.S3.bucket}")
    private String S3_BUCKET;

    private final AmazonS3Client amazonS3Client;

    private Date getExpiration(){
        Date expiration = new Date();
        expiration.setTime(expiration.getTime() + 1000 * 60 * 5); // 5분

        return expiration;
    }

    private String getFileName(String identifier){
        return identifier + "/profile-image.jpg";
    }

    public String getPresignedUrl(Member member) {
        try {
            GeneratePresignedUrlRequest generatePresignedUrlRequest =
                    new GeneratePresignedUrlRequest(S3_BUCKET, getFileName(member.getIdentifier()))
                            .withMethod(HttpMethod.PUT)
                            .withExpiration(getExpiration());

            return amazonS3Client.generatePresignedUrl(generatePresignedUrlRequest).toString();
        }catch (Exception e){
            throw new BizException(AmazonS3ResponseType.CANNOT_GET_PRESIGNED_URL);
        }
    }


    public void removeS3Image(Member member) {
        ObjectListing objectList = amazonS3Client.listObjects(S3_BUCKET, member.getIdentifier());
        List<S3ObjectSummary> objectSummaryList = objectList.getObjectSummaries();

        String[] keysList = new String[objectSummaryList.size()];
        int count = 0;
        for(S3ObjectSummary summary : objectSummaryList){
            keysList[count++] = summary.getKey();
        }

        DeleteObjectsRequest deleteObjectsRequest = new DeleteObjectsRequest(S3_BUCKET).withKeys(keysList);
        amazonS3Client.deleteObjects(deleteObjectsRequest);
    }
}
