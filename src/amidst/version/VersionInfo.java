package amidst.version;

/** Information about what each supported version is
 */
public enum VersionInfo {
	unknown(null),	
	V1_9     ("qwoomajzpb[Llp;mm[J[[Jmi"),// matches the versions 1.9, 1.9-pre4, 1.9-pre3     
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
	V1_7_4("pzozmvjs[Lmm;lg[J[[J"),
	V1_7_2("pvovmsjp[Lmj;ld[J[[J"),
	V13w39a_or_b("npmp[Lkn;jh[J[J[J[J[J[[J"),
	V13w37b_or_38a("ntmt[Lkm;jg[J[J[J[J[J[[J"),
	V13w37a("nsms[Lkl;jf[J[J[J[J[J[[J"),
	V13w36b("nkmk[Lkd;hw[J[J[J[J[J[[J"),
	V13w36a("nkmk[Lkd;hx[J[J[J[J[J[[J"),
	V1_6_4("mvlv[Ljs;hn[J[J[J[J[J[[J"),
	V1_6_2("mulu[Ljr;hm[J[J[J[J[J[[J"),
	V1_6_1("msls[Ljp;hk[J[J[J[J[J[[J"),
	V1_5_1("[Bbeabdsbawemabdtbfzbdwngngbevawfbgfawvawvaxrawbbfrausbjhaycawwaraavybkdavwbjvbkila"),
	V1_5_0("Invalid"), // TODO: This makes no sense? 1.5.0 is not on the version list!
	V1_4_6("[Baywayoaaszleaypbavaysmdazratabbaatqatqaulaswbanarnbdzauwatraohastbevasrbenbezbdmbdjkh"), // Includes 1.4.7
	V1_4_5("[Bayoaygaasrleayhbakaykmdazfassbapatjatjaueasobacarfbdoaupatkanzaslbekasjbecbenbdbbcykh"),
	V1_4_2("[Baxgawyaarjkpawzayyaxclnaxxarkazcasbasbaswargaytaqabcbathascamuardbcxarbbcpbdabbobbljy"),
	V1_3_2("[Batkatcaaofjbatdavbatgjwaubaogavfaovaovapnaocauwamxaxvapyaowajqanzayqanxayjaytaxkaxhik"),
	V1_3_1("adb"),
	V1_3pre("acl"),
	V12w27a("acs"),
	V12w26a("acl"),
	V12w25a("acg"),
	V12w24a("aca"),
	V12w23b("acg"),
	V12w22a("ace"),
	V12w21b("aby"),
	V12w21a("abm"),
	V12w19a("aau"),
	V1_2_4("[Bkivmaftxdlvqacqcwfcaawnlnlvpjclrckqdaiyxgplhusdakagi[J[Jalfqabv"), // Includes 1.2.5
	V1_2_2("wl"),
	V12w08a("wj"),
	V12w07b("wd"),
	V12w06a("wb"),
	V12w05a("vy"),
	V12w04a("vu"),
	V12w03a("vj"),
	V1_1("[Bjsudadrvqluhaarcqevyzmqmqugiokzcepgagqvsonhhrgahqfy[J[Jaitpdbo"),
	V1_0("[Baesmmaijryafvdinqfdrzhabeabexexwadtnglkqdfagvkiahmhsadk[J[Jtkgkyu"),
	V1_9pre6("uk"), // TODO: Remove these versions?
	V1_9pre5("ug"),
	V1_9pre4("uh"),  //TODO stronghold reset??
	V1_9pre3("to"),
	V1_9pre2("sv"),
	V1_9pre1("sq"),
	Vbeta_1_8_1("[Bhwqpyrrviqswdbzdqurkhqrgviwbomnabjrxmafvoeacfer[J[Jaddmkbb"); // Includes b1.8 (had to rename this enum from V1_8_1)
	
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