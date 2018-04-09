//var RSAClient= require('./rsa-client');//客户端rsa加密
//console.log(RSAClient());
//var _publicKey = new RSAClient();
const n = '	b340ca919c70636f04d6e18afb70a0d3ac0c9577ecc0d9832e8c1f71471f70363bf3c8823d907d828001f866c4dd67eca6feeca1e52a4cfdd57439aca84f968e1093acee4e1d28259c443cc40b4ac335790e8db3131fbe92cec7e3c3257598440e752f884849a8f408cda566dab38d8486cf0e9f710563a42cbecc9363ac9227';
const e = '0x10001';


var RSAClient = require('./rsa-client');//客户端rsa加密
var base64 = require('./base64');
var _publicKey = new RSAClient();
_publicKey.setPublic(n, e);
//console.log(_publicKey);

var encrypt=function(val){

    //hex2b64
    console.log(base64);
    let data = _publicKey.encrypt(val);
    console.log('888',data);
    return base64.hex2b64(_publicKey.encrypt(val));
};
let s = "eyJtZXRob2QiOiIxMTExMSIsImludXB0IjpbeyJuYW1lIjoiMSJ9LHsibmFtZSI6IjIiLCJzZXgiOiIzIn0seyJhZ2UiOjE4fV0sIm91dHB1dCI6eyJmaXJzdCI6ImEiLCJzZWNvbmQiOiJiIiwidGhyaWQiOlsxLDIsMyw0LDUsNl0sImZvdXIiOjF9LCJyZXQiOiJzdWNjZXNzIn0=";
s = s.substr(0,117);
s ="1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,";
/*
console.log(s.length,'988989')
console.log(s.length);
//s="111";
console.log(encrypt(s));

const a1 ="a06a75a3dfda12137f395caf649a9c8f03c16ec00ce158717ecba7ef7b51f108473141f2b2d18b2bd3aa0113e30a605545e63a85328c50bd73c0809907f2046ba8a4837dcf6cb5ce713c0e4bbf3f4dcf981528f27a45bc12bf81b0a6e444dd528739587c546a8b46059f2fc377ffd51d7a7d72d8e8805ff9f0510fa49d8001e9";
const a2 ="0434911cd8dd239452eb5f3a2fe16865af48ac547531a9acd0920c252fde9dc40ed1c153a53236096e467aee67517256c11f3124384c2ee57ea3c5d5b71200526e8e9b04e313edf232a3e008c1a6be91457b065fc694143ca13a93c8e8e5067896cc02e7099e2e375fcd61b11acb565e4ce3822779fbcc404bb95733975f505e" ;

const a = a1 + a2;*/

console.log('aaa',encrypt(s));
