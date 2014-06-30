(function() {
	with (this[2]) {
		with (this[1]) {
			with (this[0]) {
				return function(event) {
					(function($form, $component, event) {
						$form.drill({
							"$sys_drillcell" : "table1.A1",
							"$sys_drillto" : "ci:/collections/link/forms/default/report",
							"$sys_passparameters" : "true",
							"$sys_target" : "self"
						});
					})(_sze_form(event), _sze_component(event), event);
				};
			}
		}
	}
})



<a class="component-content" style="color:#000000;font-size:12px;font-family:宋体;" href='javascript:(function($rpt){$rpt.eval({"script":function(){$rpt.drill({
	$sys_drillto:"sailing:/analyses/个人临时测试用例/zhuchx/BI-10416/BI-10416-2",
	$sys_drillcell:"rpt1.B2",
	$sys_needFilter:true,
	$sys_passparameters:true,
        $sys_stretch:true,
	$sys_target:"dialog",
	dialogWidth:950,
	dialogHeight:450,
	target:"dialog",
	resizable:true,
	ok:function(e){
		e.dlg.getReport().submitFill({success:function(){
			$rpt.recalc();
		}});
	},
	onshow:function(e){
		e.dlg.getButtonById("ok").setCaption("确定");   
	}
});}});})(_sze_rpt());'>13,976,814.00</a>