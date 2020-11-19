package com.pedroc.appmadhacks;

import com.oracle.bmc.Region;

public class ConfigFile {
//*** LEMBRAR DE MUDAR PARA API GATEWAY ****

    //Auths OCI
    public static String TENANT_ID = "ocid1.tenancy.oc1..aaaaaaaaopi4gl6j5l5x6w54kox5l7qrnt72q4ibkw4gihmtk6raoe4r3p3q";
    public static String USER_ID = "ocid1.user.oc1..aaaaaaaacei5dw2ensnlryramjjpbtrqvncd4kofbcuesjglbzu2bwfmrcga";
    public static Region REGION = Region.US_ASHBURN_1;
    public static String FINGERPRINT = "ad:8a:c4:99:68:20:ea:a0:61:a6:98:14:3e:23:d2:d7"; //get this uploading oci_api_key_public.pem in the cloud
    public static String PRIVATEKEY = "oci_api_key.pem";
    public static String API_GATEWAY_URL = "";


    //Configs to ObjectStorage
    public static String NAMESPACE_NAME = "id4beafwqb9e";
    public static String BUCKET_NAME = "bucket-madhacks";
    public static String NOME_FOTO = "fotoergonomia.png";
    public static String OBJECT_NAME = NOME_FOTO;


}
