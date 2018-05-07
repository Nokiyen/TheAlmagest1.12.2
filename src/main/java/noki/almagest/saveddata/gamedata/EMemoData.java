package noki.almagest.saveddata.gamedata;

public enum EMemoData implements IItemData {
	
	MEMO001("almagest.gui.memo001"),
	MEMO002("almagest.gui.memo002"),
	MEMO003("almagest.gui.memo003"),
	MEMO004("almagest.gui.memo004"),
	MEMO005("almagest.gui.memo005"),
	MEMO006("almagest.gui.memo006"),
	MEMO007("almagest.gui.memo007"),
	MEMO008("almagest.gui.memo008"),
	MEMO009("almagest.gui.memo009"),
	MEMO0010("almagest.gui.memo010"),
	MEMO0011("almagest.gui.memo011"),
	MEMO0012("almagest.gui.memo012"),
	MEMO0013("almagest.gui.memo013"),
	MEMO0014("almagest.gui.memo014"),
	MEMO0015("almagest.gui.memo015"),
	MEMO0016("almagest.gui.memo016"),
	MEMO0017("almagest.gui.memo017"),
	MEMO0018("almagest.gui.memo018"),
	MEMO0019("almagest.gui.memo019"),
	MEMO0020("almagest.gui.memo020");
	
	private EMemoData(String display) {
		this.displayString = display;
	}
	
	private String displayString;
	@Override
	public String getDisplay() {
		return this.displayString;
	}
	
}
