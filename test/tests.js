/**
 * Created by Administrator on 2018/3/28.
 */
const utils = require('../RSAUtils');
assert = require('assert')
const publicKeyString = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCzQMqRnHBjbwTW4Yr7cKDTrAyVd+zA2YMujB9x\n" +
    "Rx9wNjvzyII9kH2CgAH4ZsTdZ+ym/uyh5SpM/dV0OayoT5aOEJOs7k4dKCWcRDzEC0rDNXkOjbMT\n" +
    "H76SzsfjwyV1mEQOdS+ISEmo9AjNpWbas42Ehs8On3EFY6QsvsyTY6ySJwIDAQAB";

const privateKeyString = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBALNAypGccGNvBNbhivtwoNOsDJV3\n" +
    "7MDZgy6MH3FHH3A2O/PIgj2QfYKAAfhmxN1n7Kb+7KHlKkz91XQ5rKhPlo4Qk6zuTh0oJZxEPMQL\n" +
    "SsM1eQ6NsxMfvpLOx+PDJXWYRA51L4hISaj0CM2lZtqzjYSGzw6fcQVjpCy+zJNjrJInAgMBAAEC\n" +
    "gYAHggo6CRxn/cltGqtQlsoVs5ofVJOVDEdDVtkmTH1TnOHr9xUFcRzgr23lsqBCSc0OvZS5vziD\n" +
    "WwpqwNOrhRHgNVobDgV7Vw8hJra4E3BUi1IsiqbDfpdOMpGHj219WORJEfqCllEShIlmRrbQGGbd\n" +
    "X57xci5462lcCe6hIgTMiQJBAPKM4sC8eN/qfXG2m5+Rm+9Ob9VeStIGJ6TY65DT+I4fb1UFzfPq\n" +
    "GRxH9NPoiBZql3gVVX28R5vl6m1A24pZ+MsCQQC9MV8yRnConlR2NVIX7/3+AlgjXaDqIwj452dG\n" +
    "Fbq+E6bXdX7wVih0EMni1OLeJIhKedVgwfmSL4dN2Kboh8yVAkAWns0G7IzZVSUd1cntt8azKr2D\n" +
    "SKfxrmFEHnbZqusjVgssAr6SYOK2oH9Uw/rtHEPEDzlJ4KVNBMQ4LDRNUtbBAkALqyHd7e5A2BMJ\n" +
    "f1xi0ZBuvCJHfBzSk/qXr131Op3b30Zo/3wmwomYLvjknpCndsVZIo6AezYzR+8mY/hXWxjJAkEA\n" +
    "4bjkbFAUFFR4MIMVQ2lJ6Pycb7Zu4b/CJJwA/n8MCAeECwZQLyQ6pCEwYEjCdjOiUWk/Z6PpdT6i\n" +
    "oVC83OH9vw==";
describe('RSAUtils', function () {
    var dataBundle = {
        'string': {
            data: 'ascii + 12345678',
            encoding: 'utf8'
        },
        'unicode string': {
            data: 'ascii + юникод スラ ⑨',
            encoding: 'utf8'
        },
        'empty string': {
            data: '',
            encoding: ['utf8', 'ascii', 'hex', 'base64']
        },
        'long string': {
            data: 'Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.',
            encoding: ['utf8', 'ascii']
        },
        'json object': {
            data: {str: 'string', arr: ['a', 'r', 'r', 'a', 'y', true, '⑨'], int: 42, nested: {key: {key: 1}}},
            encoding: 'json'
        },
        'json array': {
            data: [1, 2, 3, 4, 5, 6, 7, 8, 9, [10, 11, 12, [13], 14, 15, [16, 17, [18]]]],
            encoding: 'json'
        }
    };
    describe('encode data', () => {
        for (var i in dataBundle) {
            var v = dataBundle[i].data;
            console.log(v+'\t'+utils.encode(publicKeyString,v));
        }
    })

    describe("verify data",()=>{
        it('should return true',()=>{
            signResult = 'aFnWOpF58vNL86ZSj7R1EvK8J963UnYrpGLiOypSShGMiwG0luhLkg/3V2QUVDafyUm7D/6mAL3r/RpwTvwpQUNnwW+y80mpv/iS50M6JctTZ02MQcEbw2MgM1tvamg6owbGhHM6JtoegJzHlBmVc4pY74G7njCBXrWpDzA16AY=';
            signData = '987653231231456ad中国人你好啊！@￥~~！！@##￥%…………&&^^^^／／&&**())))_+_+';
            const isValid = utils.verify(publicKeyString, signData, signResult);
            console.log('签名验证结果:'+isValid);
            assert(isValid,true);
        })

    });
});