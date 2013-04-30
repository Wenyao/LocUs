package com.example.locus.security;

public interface ISecurity {
        /* This function should be called when the app comes up. This will generate a key pair
         * and returns the public key which is converted to String format. This public key will be
         * put in the chord along with the other info for other users to encrypt the data sent to this
         * node. The private key is currently kept in the memory.
         */
        public String generate_keypair() ;


        /* This function should be called when before we send a message to remote node.
         * The arguments are the message that has to be sent and the public key of the
         * Recipient. Will return the encrypted data in String format converted to Base64 encoding
         */
        public String encrypt_data(String data, String pKey) ;

        /* When we receive encrypted data from a remote node(encrypted using local nodes public key), we
         * use this function to decrypt the message and return the decrypted message in String format
         */
        public String decrypt_data(String enc_data);
}