package amidst.version;

/** Information about what each supported version is
 */
public enum VersionInfo {
	// Make sure UNKNOWN is the first entry, so it is always considered newer than all other versions, since an unknown version is most likely a new snapshot.
	unknown(null),	
	V1_13_pre2      ("ahixdtlnxxp[Lsn;uz[J[[Juvuhufbvayywlpcpgcfvozpa"),                                         // matches the launcher version id: 1.13-pre2
	V1_13_pre1      ("ahhxctknwxo[Lsm;uy[J[[Juuuguebuayxwkpbpfcfsoyoz"),                                         // matches the launcher version id: 1.13-pre1
	V18w22c         ("ahfxctknwxo[Lsm;uy[J[[Juuuguebuayvwkpbpfcfqoyoz"),                                         // matches the launcher version id: 18w22c
	V18w21b         ("ahdxbtjnvxn[Lsl;ux[J[[Jutufudbtaytwjpapecfooxoy"),                                         // matches the launcher version id: 18w21b
	V18w20c         ("ahcxatinuxm[Lsk;uw[J[[Jusueucbtayswiozpdcexowox"),                                         // matches the launcher version id: 18w20c
	V18w19b         ("agwwzthntxm[Lsj;uv[J[[Jurudubbsaymwhoypccerovow"),                                         // matches the launcher version id: 18w19b
	V18w16a         ("aavwutfnsxf[Lsh;ut[J[[Jupubtzbsaskweowpabyhotou"),                                         // matches the launcher version id: 18w16a
	V18w15a         ("aauwttensxe[Lsh;us[J[[Juouatybsasjwdowpabxrotou"),                                         // matches the launcher version id: 18w15a
	V18w14b         ("aauwttensxe[Lsh;us[J[[Juouatybsasawdowpabwyotou"),                                         // matches the launcher version id: 18w14b       
	V18w11a         ("aaqwqtbnpxb[Lse;up[J[[Jultxtvbparwwaotoxbwroqor"),                                         // matches the launcher version id: 18w11a       
	V18w10d         ("aaqwqtbnpxb[Lse;up[J[[Jultxtvbparvwaotoxbwmoqor"),                                         // matches the launcher version id: 18w10d       
	V18w09a         ("aakwlswnkww[Lrz;uk[J[[Jugtstqbparovvooosbvkolom"),                                         // matches the launcher version id: 18w09a       
	V18w08b         ("aaiwjsuniwu[Lrx;ui[J[[Juetqtobparmvtomoqbvdojok"),                                         // matches the launcher version id: 18w08b       
	V18w07c         ("aahwistniwt[Lrx;uh[J[[Judtptnbparfvsomoqbuhojok"),                                         // matches the launcher version id: 18w07c       
	V18w06a         ("aalwlswniwx[Lsa;uk[J[[Jugtstqbpargvvonorbuaokol"),                                         // matches the launcher version id: 18w06a       
	V18w05a         ("znvssdnfwe[Lrs;tr[J[[Jtnszsxbnaqgvcoiombliofog"),                                          // matches the launcher version id: 18w05a       
	V18w03b         ("zjvorznfwa[Lro;tn[J[[Jtjsvstbnaqcuyoibleofog"),                                            // matches the launcher version id: 18w03b       18w02a       
	V18w01a         ("zhvnrynevz[Lrn;tm[J[[Jtisussbnapsuxohbkyoeof"),                                            // matches the launcher version id: 18w01a       
	V17w50a         ("ykutremkvf[Lqt;ss[J[[Jsosarybnaovud"),                                                     // matches the launcher version id: 17w50a       
	V17w49b         ("yiusrdmjve[Lqs;sr[J[[Jsnrzrxbnaoquc"),                                                     // matches the launcher version id: 17w49b       
	V17w48a         ("xvugqxmdus[Lqm;sl[J[[Jshrtrrblaoe"),                                                       // matches the launcher version id: 17w48a       
	V17w47b         ("xuufqwmcur[Lql;sk[J[[Jsgrsrqbl"),                                                          // matches the launcher version id: 17w47b       
	V17w46a         ("xiugqslyut[Lqh;sg[J[[Jscrormbl"),                                                          // matches the launcher version id: 17w46a       
	V17w45b         ("wvttqflmug[Lpu;rt[J[[Jrprbqzbl"),                                                          // matches the launcher version id: 17w45b       
	V17w43b         ("vosnozmtta[Loo;qn[J[[Jqjpvpt"),                                                            // matches the launcher version id: 17w43b       
	V1_12_2         ("ulrlozmtry[Loo;pl[J[[Jph"),                                                                // matches the launcher version id: 1.12.2       1.12.1       
	V1_12           ("ujrjoxmsrw[Lom;pj[J[[Jpf"),                                                                // matches the launcher version id: 1.12         
	V1_12_pre2      ("uhrhovmqru[Lok;ph[J[[Jpd"),                                                                // matches the launcher version id: 1.12-pre2    
	V1_12_pre1      ("ugrgoumprt[Loj;pg[J[[Jpc"),                                                                // matches the launcher version id: 1.12-pre1    
	V17w18b         ("tyqyommirl[Lob;oy[J[[Jou"),                                                                // matches the launcher version id: 17w18b       
	V17w17b         ("tpqroemare[Lnt;oq[J[[Jom"),                                                                // matches the launcher version id: 17w17b       
	V17w16b         ("tnqpoclyrc[Lnr;oo[J[[Jok"),                                                                // matches the launcher version id: 17w16b       
	V17w15a         ("tlqnoalwra[Lnp;om[J[[Joi"),                                                                // matches the launcher version id: 17w15a       
	V17w14a         ("tkqmoalwqz[Lnp;om[J[[Joi"),                                                                // matches the launcher version id: 17w14a       
	V17w13b         ("tgqinwlsqv[Lnl;oi[J[[Joe"),                                                                // matches the launcher version id: 17w13b       
	V1_11_2         ("rsoumhkfph[Llw;mt[J[[Jmp"),                                                                // matches the launcher version id: 1.11.2       1.11.1       
	V1_11           ("rroumhkfph[Llw;mt[J[[Jmp"),                                                                // matches the launcher version id: 1.11         1.11-pre1    
	V16w44a         ("rqotmgkfpg[Llv;ms[J[[Jmo"),                                                                // matches the launcher version id: 16w44a       
	V16w43a         ("rpotmgkfpg[Llv;ms[J[[Jmo"),                                                                // matches the launcher version id: 16w43a       16w42a       16w41a       16w40a       16w39c       
	V16w38a         ("rlosmfkepf[Llu;mr[J[[Jmn"),                                                                // matches the launcher version id: 16w38a       
	V16w36a         ("rkosmfkepf[Llu;mr[J[[Jmn"),                                                                // matches the launcher version id: 16w36a       
	V16w35a         ("rjosmfkepf[Llu;mr[J[[Jmn"),                                                                // matches the launcher version id: 16w35a       16w33a       16w32b       
	V1_10_2         ("rboqmdkcpd[Lls;mp[J[[Jml"),                                                                // matches the launcher version id: 1.10.2       1.10.1       1.10         
	V16w21b         ("qzopmckbpc[Llr;mo[J[[Jmk"),                                                                // matches the launcher version id: 16w21b       
	V16w20a         ("qxopmckbpc[Llr;mo[J[[Jmk"),                                                                // matches the launcher version id: 16w20a       
	V1_9_4          ("qwoombkapb[Llq;mn[J[[Jmj"),                                                                // matches the launcher version id: 1.9.4        1.9.3        
	V1_9_2          ("qwoomajzpb[Llp;mm[J[[Jmi"),                                                                // matches the launcher version id: 1.9.2        1.9.1        1.9        1.9-pre4        1.9-pre3       
	V1_9_pre2("qvoomajzpb[Llp;mm[J[[Jmi"),// matches the versions 1.9-pre2, 1.9-pre1, 16w07b, 16w06a, 16w05b, 16w04a, 16w03a and 16w02a. Which is a shame because the stronghold alg is different from 16w06a onward      
	V15w51b("quonmajzpa[Llp;mm[J[[Jmi"),  // matches the versions 15w51b and 15w51a      
	V15w50a("qtonmajzpa[Llp;mm[J[[Jmi"),  // matches the versions 15w50a, 15w49b, and 15w47c      
	V15w46a("qsonmajzpa[Llp;mm[J[[Jmi"),  // matches the versions 15w46a      
	V15w45a("qtoombkapb[Llq;mn[J[[Jmj"),  // matches the versions 15w45a and 15w44b      
	V15w43c("qsoombkapb[Llq;mn[J[[Jmj"),  // matches the versions 15w43c      
	V15w42a("qnojlzjzow[Llp;ml[J[[Jmh"),  // matches the versions 15w42a      
	V15w41b("qmoilyjyov[Llo;mk[J[[Jmg"),  // matches the versions 15w41b      
	V15w40b("qhoelujuor[Llk;mg[J[[Jmc"),  // matches the versions 15w40b, 15w39c, 15w38b, and 15w37a      
	V15w36d("qgodltjuoq[Lll;mf[J[[Jmb"),  // matches the versions 15w36d      
	V15w35e("qeoclsjuop[Llk;me[J[[Jma"),  // matches the versions 15w35e      
	V15w34d("qdoblsjuoo[Lll;me[J[[Jma"),  // matches the versions 15w34d      
	V15w33c("qanzlrjtom[Llk;md[J[[Jlz"),  // matches the versions 15w33c      
	V15w32c("pmnvlnjt[Llg;lz[J[[Jlv"),    // matches the versions 15w32c      
	V15w31c("oxnvlnjt[Llg;lz[J[[Jlv"),    // matches the versions 15w31c      	
	V1_8_9("orntlljs[Lle;lx[J[[Jlt"),     // 1.8.4, 1.8.5, 1.8.6, 1.8.7, 1.8.8 and 1.8.9 all have the same typeDump version ID. They are all security issue fixes.
	V1_8_3("osnulmjt[Llf;ly[J[[Jlu"),     // 1.8.3 and 1.8.2 have the same typeDump version ID - probably because 1.8.2 -> 1.8.3 was a fix for a server-side bug (https://mojang.com/2015/02/minecraft-1-8-2-is-now-available/)
	V1_8_1("wduyrdnq[Lqu;sp[J[[Jsa"),
	V1_8("wbuwrcnp[Lqt;sn[J[[Jry"),
	V14w21b("tjseoylw[Loq;qd[J[[Jpo"),
	V1_7_10("riqinckb[Lmt;oi[J[[Jns"),
	V1_7_9("rhqhnbkb[Lms;oh[J[[Jnr"),
	V14w02a("qrponkki[Lnb;lv[J[[J"),
	V1_7_5("qfpfnbjy[Lms;lm[J[[J"),       // matches the launcher version id: 1.7.5        
	V1_7_4("pzozmvjs[Lmm;lg[J[[J"),
	V1_7_2("pvovmsjp[Lmj;ld[J[[J"),
	V13w39a_or_b("npmp[Lkn;jh[J[J[J[J[J[[J"),
	V13w37b_or_38a("ntmt[Lkm;jg[J[J[J[J[J[[J"),
	V13w37a         ("nsms[Lkl;jf[J[J[J[J[J[[J"),                                                                // matches the launcher version id: 
	V13w36b         ("nkmk[Lkd;hw[J[J[J[J[J[[J"),                                                                // matches the launcher version id: 
	V13w36a         ("nkmk[Lkd;hx[J[J[J[J[J[[J"),                                                                // matches the launcher version id: 
	V1_6_4          ("mvlv[Ljs;hn[J[J[J[J[J[[J"),                                                                // matches the launcher version id: 1.6.4        
	V1_6_2          ("mulu[Ljr;hm[J[J[J[J[J[[J"),                                                                // matches the launcher version id: 1.6.2        
	V1_6_1          ("msls[Ljp;hk[J[J[J[J[J[[J"),                                                                // matches the launcher version id: 1.6.1        
	V1_5_2          ("[Bbdzbdrbawemabdsbfybdvngngbeuawfbgeawvawvaxrawbbfqausbjgaycawwaraavybkcavwbjubkila"),     // matches the launcher version id: 1.5.2        
	V1_5_1          ("[Bbeabdsbawemabdtbfzbdwngngbevawfbgfawvawvaxrawbbfrausbjhaycawwaraavybkdavwbjvbkila"),     // matches the launcher version id: 1.5.1        
	V1_4_7          ("[Baywayoaaszleaypbavaysmdazratabbaatqatqaulaswbanarnbdzauwatraohastbevasrbenbezbdmbdjkh"), // matches the launcher version id: 1.4.7        1.4.6        
	V1_4_5          ("[Bayoaygaasrleayhbakaykmdazfassbapatjatjaueasobacarfbdoaupatkanzaslbekasjbecbenbdbbcykh"), // matches the launcher version id: 1.4.5        1.4.4        
	V1_4_2          ("[Baxgawyaarjkpawzayyaxclnaxxarkazcasbasbaswargaytaqabcbathascamuardbcxarbbcpbdabbobbljy"), // matches the launcher version id: 1.4.2        
	V1_3_2          ("[Batkatcaaofjbatdavbatgjwaubaogavfaovaovapnaocauwamxaxvapyaowajqanzayqanxayjaytaxkaxhik"), // matches the launcher version id: 1.3.2        
	V1_3_1          ("[Batjatbaaoejaatcavaatfjvauaaofaveaouaouapmaobauvamwaxuapxaovajpanyaypanwayiaysaxjaxgij"), // matches the launcher version id: 1.3.1        
	V1_3pre         ("acl"),                                                                                     // matches the launcher version id: 
	V12w27a         ("acs"),                                                                                     // matches the launcher version id: 
	V12w26a         ("acl"),
	V12w25a         ("acg"),                                                                                     // matches the launcher version id: 
	V12w24a         ("aca"),                                                                                     // matches the launcher version id: 
	V12w23b         ("acg"),
	V12w22a         ("ace"),                                                                                     // matches the launcher version id: 
	V12w21b         ("aby"),                                                                                     // matches the launcher version id: 
	V12w21a         ("abm"),                                                                                     // matches the launcher version id: 
	V12w19a         ("aau"),                                                                                     // matches the launcher version id: 
	V1_2_5          ("[Bkivmaftxdlvqacqcwfcaawnlnlvpjclrckqdaiyxgplhusdakagi[J[Jalfqabv"),                       // matches the launcher version id: 1.2.5        1.2.4        
	V1_2_3          ("[Bkfviafowzlvmaclcueyaarninivlizlocipzaisxcphhrrzajugf[J[Jakzpwbt"),                       // matches the launcher version id: 1.2.3        1.2.2        1.2.1        
	V1_2_2          ("wl"),
	V12w08a         ("wj"),                                                                                      // matches the launcher version id: 
	V12w07b         ("wd"),                                                                                      // matches the launcher version id: 
	V12w06a         ("wb"),                                                                                      // matches the launcher version id: 
	V12w05a         ("vy"),                                                                                      // matches the launcher version id: 
	V12w04a         ("vu"),                                                                                      // matches the launcher version id: 
	V12w03a         ("vj"),                                                                                      // matches the launcher version id: 
	V1_1            ("[Bjsudadrvqluhaarcqevyzmqmqugiokzcepgagqvsonhhrgahqfy[J[Jaitpdbo"),                        // matches the launcher version id: 1.1          
	V1_0            ("[Baesmmaijryafvdinqfdrzhabeabexexwadtnglkqdfagvkiahmhsadk[J[Jtkgkyu"),                     // matches the launcher version id: 1.0          
	Vbeta_1_9_pre6  ("uk"),                                                                                      // matches the launcher version id: 
	Vbeta_1_9_pre5  ("ug"),                                                                                      // matches the launcher version id: 
	Vbeta_1_9_pre4  ("uh"),                                                                                      // matches the launcher version id: 
	Vbeta_1_9_pre3  ("to"),                                                                                      // matches the launcher version id: 
	Vbeta_1_9_pre2  ("sv"),                                                                                      // matches the launcher version id: 
	Vbeta_1_9_pre1  ("sq"),                                                                                      // matches the launcher version id: 
	Vbeta_1_8_1     ("[Bhwqpyrrviqswdbzdqurkhqrgviwbomnabjrxmafvoeacfer[J[Jaddmkbb"),                            // matches the launcher version id: b1.8.1       b1.8         
	Vbeta_1_7_3     ("[Bobcxpyfdndclsdngrjisjdamkpxczvuuqfhvfkvyovyik[J[Jxivscg"),                               // matches the launcher version id: b1.7.3       b1.7.2       b1.7         
	Vbeta_1_6_6     ("[Bnxcvpufbmdalodlgpjfsecymgptcxvmukffuxkryfvqih[J[Jwzvkce"),                               // matches the launcher version id: b1.6.6       b1.6.5       b1.6.4       b1.6.3       b1.6.2       b1.6.1       b1.6         
	Vbeta_1_5_01    ("nfcpozetmcukwdfggiprfcslooycruntlextyjzxeurhv[J[Jvyulbz"),                                 // matches the launcher version id: b1.5_01      b1.5         
	Vbeta_1_4_01    ("lncdmxebichjmcsfkhooxcfkcmwcerqqvefrkisujsbgw[J[Jtervbo"),                                 // matches the launcher version id: b1.4_01      
	Vbeta_1_4       ("lncdmxebichjmcsfkhooxcfkcmwcerpqvefrkisujsagw[J[Jterubo"),                                 // matches the launcher version id: b1.4         
	Vbeta_1_3_01    ("kybymidthccizcnfbhfoicbjpmhbzqfdxquigtmrhgn[J[Jrbbk"),                                     // matches the launcher version id: b1.3_01      
	Vbeta_1_3b      ("kybymidthccizcnfbhfoicbjpmhbzqgdxqvigtnrign[J[Jrcbk"),                                     // matches the launcher version id: b1.3b        
	Vbeta_1_2_02    ("kbbvlmdnhbzcjesgsnhbyiwllbwpedrprhqsgqega[J[Jpybj"),                                       // matches the launcher version id: b1.2_02      b1.2_01      b1.2         
	Vbeta_1_1_02    ("jjboksddfbsccehgemjbrifkrbpobdhonhbqvoyfo[J[Joubc"),                                       // matches the launcher version id: b1.1_02      b1.1_01      
	Vbeta_1_0_2     ("jibokrddfbscceggdmibriekqbpoadhomhaquoxfn[J[Jotbc"),                                       // matches the launcher version id: b1.0.2       b1.0_01      b1.0         
	Valpha_1_2_6    ("ivbmkccyfbqbzeafulsbphukbbnnldcnxgqqgoiff[J[Joeba"),                                       // matches the launcher version id: a1.2.6       
	Valpha_1_2_5    ("iubmkbcxfbqbydzftlrbphtkabnnkdbnwgpqfohfe[J[Jodba"),                                       // matches the launcher version id: a1.2.5       a1.2.4_01    
	Valpha_1_2_3_04 ("iubmkbcxfbqbydzftlqbphtkabnnjdbnvgpqeogfe[J[Jocba"),                                       // matches the launcher version id: a1.2.3_04    a1.2.3_02    a1.2.3_01    a1.2.3       
	Valpha_1_2_2b   ("isbmjycwfbqbydyfrlnbphrjxbnngdansgnqbodfd[J[Jnzba"),                                       // matches the launcher version id: a1.2.2b      a1.2.2a      
	Valpha_1_2_1_01 ("imbkjrcudbobwdufmlgbnhmjqblmzcynlgiptnv[J[Jnray"),                                         // matches the launcher version id: a1.2.1_01    a1.2.1       a1.2.0_02    a1.2.0_01    a1.2.0       
	Valpha_1_1_2_01 ("hqbeircnebibqdleykdbhgriqbflucrmffrofmp[Jmlat"),                                           // matches the launcher version id: a1.1.2_01    a1.1.2       
	Valpha_1_1_0    ("hqbeircnebibqdleykdbhgriqbflucrmffroemo[Jmlat"),                                           // matches the launcher version id: a1.1.0       
	Valpha_1_0_17_04("hpbdiqcmebhbpdkexkbbggqipbeltcqmdfqobmm[Jmjar"),                                           // matches the launcher version id: a1.0.17_04   a1.0.17_02   
	Valpha_1_0_16   ("hgazihcjebebmdferjtbdgiigbblkcnlvfinrmd[Jmbap"),                                           // matches the launcher version id: a1.0.16      
	Valpha_1_0_15   ("hfazigcjebebmdferjsbdgiifbbljcnlufinqmc[Jmaap"),                                           // matches the launcher version id: a1.0.15      
	Valpha_1_0_14   ("hcazidcjebebmdfeqjpbdghicbblfcnlpfhnmly[Jlwap"),                                           // matches the launcher version id: a1.0.14      
	Valpha_1_0_11   ("haaziacjebebmddenjlbdgfhzbbkzcnljfenels[Jlqap");                                           // matches the launcher version id: a1.0.11      
	
	public final String versionId;
	
	VersionInfo(String versionId) {
		this.versionId = versionId;
	}
	
	public boolean saveEnabled() {
		return this != V12w21a && this != V12w21b && this != V12w22a;
	}
	
	@Override
	public String toString() {
		return super.toString().replace("_", ".");
	}
	
	public boolean isAtLeast(VersionInfo other) {
		return this.ordinal() <= other.ordinal(); 
	}
}