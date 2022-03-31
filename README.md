## Azure WebApp Java Springboot accessing KeyVault using ManagedIdentity

This sample will fetch a secret or a key or a certificate from Azure keyvault 
Authentication will be by Managed Services Identity 

```bash
           String msiEndpoint =  System.getenv("MSI_ENDPOINT");
           String msiSecret =  System.getenv("MSI_SECRET");
           AppServiceMSICredentials msiCredentials = new AppServiceMSICredentials(AzureEnvironment.AZURE,msiEndpoint, msiSecret);
           KeyVaultClient keyVaultClient = new KeyVaultClient(msiCredentials);
           
           String keyVaultName = "<your-keyVault-name>";
		   String keyVaultUri = "https://" + keyVaultName + ".vault.azure.net";
		   CertificateClient certificateClient = new CertificateClientBuilder().vaultUrl  (keyVaultUri).credential(new DefaultAzureCredentialBuilder().build()).buildClient();
```

In the above code, replace "your-keyVault-name" with your vault name.

In Azure portal for the Webapp, turn on Identity and enable Azure Role Assignments (preview)
-or-
navigate to the Keyvault in portal and add new access policy

Browse the app using the Web app URL
 
