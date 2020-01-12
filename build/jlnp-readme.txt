The next step consists of compressing the desired application into a JAR file and signing it with a desired signature key.
To compress the program and all of its required components into a JAR file, place all parts of the program in the same directory,
then perform the following command-line from that same directory:

jar cf JNLP_WhatIsMyIP.jar WhatIsMyIP.class

Next,you would want to sign the JAR file for two reasons. The first is to copyright and secure the JAR file from third
party modification. The second reason is to provide information about the author of this package. The following command-line creates 
the signature key. A prompt will follow asking you for some information including, at the least,your first and last names.

keytool -genkey -keystore mySignatureKey -alias jdc

The last step in this stage is to use this signature key and sign the JAR file. The following command-line will perform thistask.
A prompt will follow asking for the same password as the one used during the creation of the signature key.

jarsigner -keystore mySignatureKey JNLP_WhatIsMyIP.jar jdc


