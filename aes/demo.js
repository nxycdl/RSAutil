/**
 * Created by Administrator on 2018/4/8.
 */
const CryptoJS = require("./lib/core");
const AES = require('./lib/aes-min');
var data = [{id: 1}, {id: 2}]

// Encrypt
var ciphertext = AES.encrypt(JSON.stringify(data), 'secret key 123');
console.log(ciphertext);
