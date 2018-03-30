/**
 * Created by Administrator on 2018/3/28.
 */
const rsa = require("jsrsasign");
const NodeRSA = require('node-rsa');
module.exports = {
    /**
     *用于后台验证是否是后台返回的数据;
     * @param publicKey 公钥
     * @param data      签名数据
     * @param signResult    签名结果;
     */
    verify: (publicKeyString, signData, signResult) => {
        const publicKeyStringPEM = '-----BEGIN PUBLIC KEY-----\n' + publicKeyString + '\n-----END PUBLIC KEY-----';
        const publicKey = rsa.KEYUTIL.getKey(publicKeyStringPEM);
        const sig = new rsa.Signature({alg: "MD5withRSA"});
        sig.init(publicKey);
        sig.updateString(signData);
        const sigHex = rsa.b64tohex(signResult);
        const isValid = sig.verify(sigHex);
        return isValid == true ? true : false;
    },
    /**
     * 使用公钥对data进行加密;
     * @param publicKeyString
     * @param data
     * @return {string|Buffer}  返回base64格式的密文;
     */
    encode: (publicKeyString, data) => {
        const publicKeyStringPEM = '-----BEGIN PUBLIC KEY-----\n' + publicKeyString + '\n-----END PUBLIC KEY-----';
        const key = new NodeRSA({b: 1024});
        key.importKey(publicKeyStringPEM, 'pkcs8-public');
        key.setOptions({encryptionScheme: 'pkcs1'});
        return key.encrypt(data, 'base64');
    }
}