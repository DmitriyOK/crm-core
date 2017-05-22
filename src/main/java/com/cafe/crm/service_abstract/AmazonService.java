package com.cafe.crm.service_abstract;


import com.amazonaws.services.s3.AmazonS3;

import java.io.File;

public interface AmazonService {

	AmazonS3 getConnection();

	void putObject(AmazonS3 s3client, File file, String bucketName, String keyName);

	void getObject(AmazonS3 s3client, String bucketName, String keyName);

	void saveObjectToFile(AmazonS3 s3client, File file, String bucketName, String keyName);

}
