package net.runelite.client.plugins.externals.oneclick.pojos;

import lombok.Value;

@Value
public class CustomItem
{
	int useThisId;
	String useThisName;
	int onThisId;
	String onThisName;

	public String getTargetString()
	{
		return "<col=ff9040>" + this.getUseThisName() + "</col><col=ffffff> -> <col=ff9040>" + this.getOnThisName() + "</col>";
	}
}
