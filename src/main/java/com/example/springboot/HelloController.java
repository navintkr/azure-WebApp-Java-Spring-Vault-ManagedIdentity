package com.example.springboot;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.microsoft.azure.AzureEnvironment;
import com.microsoft.azure.credentials.AppServiceMSICredentials;
import com.microsoft.azure.keyvault.KeyVaultClient;
import com.azure.security.keyvault.certificates.CertificateClient;
import com.azure.security.keyvault.certificates.CertificateClientBuilder;
import com.azure.security.keyvault.certificates.models.CertificateOperation;
import com.azure.security.keyvault.certificates.models.CertificatePolicy;
import com.azure.security.keyvault.certificates.models.DeletedCertificate;
import com.azure.security.keyvault.certificates.models.KeyVaultCertificate;
import com.azure.security.keyvault.certificates.models.KeyVaultCertificateWithPolicy;
import com.azure.identity.DefaultAzureCredentialBuilder;
import com.microsoft.azure.keyvault.models.SecretBundle;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Objects;

import java.io.File;
import java.util.List;
import java.io.FileOutputStream;

@RestController
public class HelloController {
	private static final String MSI_ENDPOINT_ENV_VARIABLE = "MSI_ENDPOINT";
    private static final String MSI_SECRET_ENV_VARIABLE = "MSI_SECRET";
    private KeyVaultClient keyVaultClient;
	@GetMapping("/")
	public String index() {
		
		// After enabling managed identity - these are pulled from env
		String msiEndpoint =  System.getenv(MSI_ENDPOINT_ENV_VARIABLE);
        String msiSecret =  System.getenv(MSI_SECRET_ENV_VARIABLE);
        AppServiceMSICredentials msiCredentials = new AppServiceMSICredentials(AzureEnvironment.AZURE,msiEndpoint, msiSecret);

		// Get Key Vault secret and Key
        keyVaultClient = new KeyVaultClient(msiCredentials);
		String secret = keyVaultClient.getSecret("https://<your-keyVault-name>.vault.azure.net/","<your-secret-name>").toString();
        String key = keyVaultClient.getKey("https://<your-keyVault-name>.vault.azure.net/","<your-key-name>").toString();
		String certName = "abc";

		// Get Key Vault Certificate
		String keyVaultName = "<your-keyVault-name>";
		String keyVaultUri = "https://" + keyVaultName + ".vault.azure.net";
		CertificateClient certificateClient = new CertificateClientBuilder().vaultUrl(keyVaultUri).credential(new DefaultAzureCredentialBuilder().build()).buildClient();
		KeyVaultCertificate retrievedCertificate = certificateClient.getCertificate(certName);
		// You can now access the details of the retrieved certificate with operations like retrievedCertificate.getName, retrievedCertificate.getProperties, etc. As well as its contents retrievedCertificate.getCer.
		return "my key : "+key+" my secret : "+secret;
		//return "Greetings from Spring Boot!";
	}

}
