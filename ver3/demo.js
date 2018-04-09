/**
 * Created by Administrator on 2018/4/4.
 */
const JSEncrypt = require('./jsencrypt');
var encrypt = new JSEncrypt();
const publicKeyString = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCzQMqRnHBjbwTW4Yr7cKDTrAyVd+zA2YMujB9x\n" +
    "Rx9wNjvzyII9kH2CgAH4ZsTdZ+ym/uyh5SpM/dV0OayoT5aOEJOs7k4dKCWcRDzEC0rDNXkOjbMT\n" +
    "H76SzsfjwyV1mEQOdS+ISEmo9AjNpWbas42Ehs8On3EFY6QsvsyTY6ySJwIDAQAB";
const publicKeyStringPEM = '-----BEGIN PUBLIC KEY-----\n' + publicKeyString + '\n-----END PUBLIC KEY-----';
encrypt.setPublicKey(publicKeyStringPEM);