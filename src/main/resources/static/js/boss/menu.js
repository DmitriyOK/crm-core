function showModal(id) {
	var modal = $('.dima_modal');
	modal.find("#d_id").val(id);
	$.ajax({
		type: "POST",
		url: "/boss/menu/getProduct",
		data: ({
			id: id
		}),
		success: function (product) {

			modal.find("#d_name").val(product.name);
			modal.find("#d_des").val(product.description);
			modal.find("#d_cost").val(product.cost);

		},
		error: function (e) {

			alert("Error show modal ");
		}
	});

	modal.fadeIn();
}

function createProd(id) {
	var setName = $("#addName" + id).val();
	var setDes = $("#addDes" + id).val();
	var setCost = $("#addCost" + id).val();

	var ingredient = [];
	var amount = [];
	var myMap = new Object();
	var i;

	var table_parent = $(document).find('#recipeTable' + id);

	table_parent.find('.mySelect option:selected').each(function () {
		ingredient.push($(this).val());
	});
	table_parent.find('.inputClassTest').each(function () {

		amount.push($(this).val());
	});

	for (i = 0; i < ingredient.length; i++) {
		myMap[ingredient[i]] = amount[i];
	}

	var wrapper = {
		idCat: id,
		name: setName,
		description: setDes,
		cost: setCost,
		names: ingredient,
		amount: amount
	};
	$.ajax({
		type: "POST",
		url: "/boss/menu/addAjax",
		contentType: "application/json; charset=utf-8",
		dataType: "json",
		data:JSON.stringify(wrapper),
		success: function (result) {
            var recipeButton = '<button form="getRecipePage" name="id" class="btn btn-primary btn-info" value="'+result+'" th:type="submit"   >Изменить рецепт </button>';
			var editButton = '<a id="ins"  class="btn btn-primary btn-info" onclick="showModal(' + result + ')"  data-toggle="modal" >Редактировать </a>';
			var delButton = '<a id="del"  class="btn btn-primary btn-danger" onclick="del2(' + result + ')"   >Удалить </a>';
			var tr = '<tr id="tr' + result + '"><td>#</td><td id="b' + result + '"><p id="E' + result + '">' + setName + '</p></td><td id="c' + result + '">' + setDes + '</td><td id="d' + result + '">' + setCost + '</td><td>' + editButton + '</td><td>' + recipeButton + '</td><td>' + delButton + '</td></tr>';
			var allTR = '<tr id="allTR' + result + '"><td>#</td><td id="allB' + result + '">' + setName + '</td><td id="allC' + result + '">' + setDes + '</td><td id="allD' + result + '">' + setCost + '</td><td>' + editButton + '</td><td>' + recipeButton + '</td><td>' + delButton + '</td></tr>';
			$('#qwe' + id + ' tr:last').after(tr);
			$('#allTable' + ' tr:last').after(allTR);
		},
		error: function (e) {
			alert("Неверный формат данных");
		}
	});
}

function closeModal() {
	var modal = $('.dima_modal');
	modal.fadeOut();
}

function editProdModal() {
	var formData = {
		id: $("#d_id").val(),
		name: $("#d_name").val(),
		description: $("#d_des").val(),
		cost: $("#d_cost").val(),
	}

	var cst = $("#d_cost").val();
	var nme = $("#d_name").val();
	var id = $("#d_id").val();

	if (!isNaN(+cst)) {
		if ("" != nme) {
			$.ajax({
				type: "POST",
				contentType: "application/json",
				url: "/boss/menu/updProd",
				data: JSON.stringify(formData),
				dataType: 'json',
				success: function (result) {
					$("#b" + id).html(result.name);
					$("#c" + id).html(result.description);
					$("#d" + id).html(result.cost);
					$("#allB" + id).html(result.name);
					$("#allC" + id).html(result.description);
					$("#allD" + id).html(result.cost);
					closeModal()
				},
				error: function (e) {
					alert("Неверный формат данных!")
				}
			});
		}
		else {
			alert("Неверный формат названия!")
		}
	}
	else {
		alert("Неверный формат цены!")
	}
}

function del(id) {
	var formData = {
		del: id,
	}
	$.ajax({
		type: "POST",
		url: "/boss/menu/deleteProduct",
		data: formData,
		success: function (result) {
			$("#tr" + id).remove();
			$("#allTR" + id).remove();
		},
		error: function (e) {
		}
	});
}

function del2(id) {
	var formData = {
		del: id,
	}
	$.ajax({
		type: "POST",
		url: "/boss/menu/deleteProduct",
		data: formData,
		success: function (result) {
			$(document).find("#tr" + id).remove();
			$(document).find("#allTR" + id).remove();
		},
		error: function (e) {
		}
	});
}

function editProd(id) {
	var formData = {
		id: $("#updId" + id).val(),
		name: $("#updName" + id).val(),
		description: $("#updDes" + id).val(),
		cost: $("#updCost" + id).val(),
	}
	var cst = $("#updCost" + id).val();
	var nme = $("#updName" + id).val();

	if (!isNaN(+cst)) {

		if ("" != nme) {
			$.ajax({
				type: "POST",
				contentType: "application/json",
				url: "/boss/menu/updProd",
				data: JSON.stringify(formData),
				dataType: 'json',
				success: function (result) {

					$("#b" + id).html(result.name);
					$("#c" + id).html(result.description);
					$("#d" + id).html(result.cost);
					$("#allB" + id).html(result.name);
					$("#allC" + id).html(result.description);
					$("#allD" + id).html(result.cost);
				},
				error: function (e) {

					alert("Неверный формат данных!  1")
				}
			});
		}
		else {
			alert("Неверный формат названия! 2")
		}
	}
	else {
		alert("Неверный формат цены! 3")
	}
}

$(document).ready(function () {

	(function ($) {

		$('#filter').keyup(function () {

			var rex = new RegExp($(this).val(), 'i');
			$('.searchable tr').hide();
			$('.searchable tr').filter(function () {
				return rex.test($(this).text());
			}).show();
		})
	}(jQuery));
});

$(document).ready(function () {
	$("#myTab a").click(function (e) {
		e.preventDefault();
		$(this).tab('show');
	});
});

$('.super').click(function () {
	var pageName = $(this).data('page');
	// ajax-запрос
	$.ajax({
		url: '/boss/menu/upd',
		cache: false,
		success: function (html) {

			$("#bossview").html(html);
		}
	});
});

function showModal(id) {
	var modal = $('.dima_modal');
	modal.find("#d_id").val(id);
	$.ajax({
		type: "POST",
		url: "/boss/menu/getProduct",
		data: ({
			id: id
		}),
		success: function (product) {

			modal.find("#d_name").val(product.name);
			modal.find("#d_des").val(product.description);
			modal.find("#d_cost").val(product.cost);

		},
		error: function (e) {
			alert("Ошибка ;( ");
			console.log("ERROR: ", e);
		}
	});

	modal.fadeIn();
}
function editProdAll(id) {

	var formData = {
		id: $("#allId" + id).val(),
		name: $("#allName" + id).val(),
		description: $("#allDes" + id).val(),
		cost: $("#allCost" + id).val(),
	}
	var cst = $("#allCost" + id).val();
	var nme = $("#allName" + id).val();

	if (!isNaN(+cst)) {

		if ("" != nme) {
			$.ajax({
				type: "POST",
				contentType: "application/json",
				url: "/boss/menu/updProd",
				data: JSON.stringify(formData),
				dataType: 'json',
				success: function (result) {

					$("#b" + id).html(result.name);
					$("#c" + id).html(result.description);
					$("#d" + id).html(result.cost);
					$("#allB" + id).html(result.name);
					$("#allC" + id).html(result.description);
					$("#allD" + id).html(result.cost);
				},
				error: function (e) {
					alert("Неверный формат данных!")
					console.log("ERROR: ", e);
				}
			});
		}
		else {
			alert("Неверный формат названия!")
		}
	}
	else {
		alert("Неверный формат цены!")
	}
}
function closeModal() {
	var modal = $('.dima_modal');
	modal.fadeOut();
}
$(document).ready(function () {

	(function ($) {

		$('#filter').keyup(function () {

			var rex = new RegExp($(this).val(), 'i');
			$('.searchable tr').hide();
			$('.searchable tr').filter(function () {
				return rex.test($(this).text());
			}).show();
		})
	}(jQuery));
});
$(document).ready(function () {
	$("#myTab a").click(function (e) {
		e.preventDefault();
		$(this).tab('show');
	});
});
$('.super').click(function () {
	var pageName = $(this).data('page');
	// ajax-запрос
	$.ajax({
		url: '/boss/menu/upd',
		cache: false,
		success: function (html) {
			$("#bossview").html(html);
		}
	});
});

function sendLogLevel() {
	var levelMap = {level: $('#chooseLogLevel').val()};
	if ($('#chooseLogLevel').val() == 'INFO' || $('#chooseLogLevel').val() == 'ERROR'
		|| $('#chooseLogLevel').val() == 'DEBUG' || $('#chooseLogLevel').val() == 'WARN') {
		$.ajax({
			type: "POST",
			url: "/boss/property/logLevel",
			data: levelMap,
			success: function (result) {
				$('.form-group').html('Уровень логирования задан.');
			},
			error: function (e) {
				var json = '<h4 style="color:aqua">Действующий пароль введён неверно</h4>';
			}
		});

	} else {
		var json = '<h4 style="color:red">Неизвестный уровень логгирования</h4>';
		$('.modal-title').html(json);
	}
}

function newSMTPSettings() {
	var formData = {
		settingsName: $("#settingsName").val(),
		password: $("#settingsPassword").val(),
		email: $("#settingsEmail").val()
	}
	$.ajax({
		type: "POST",
		url: "/boss/settings/advert-setting/new",
		data: formData,
		success: function (result) {
			$("#successModal").modal('show')
			alert("Получилось!")
		},
		error: function (e) {
			$("#errorModal").modal('show')
			alert("Ошибка")
		}
	});
}

function applySMTPSettings(id) {
	var formData = {
		settingsId: id,
	}
	$.ajax({
		type: "POST",
		url: "/boss/settings/advert-setting/existing-settings",
		data: formData,
		success: function (result) {
			var successMessage = '<h4 style="color:green;" align="center">' + result + '</h4>';
			$('.messageAd').html(successMessage).show();
			window.setTimeout(function () {
				location.reload()
			}, 1000);
		},
		error: function (e) {
			var errorMessage = '<h4 style="color:red;" align="center">' + e.responseText + '</h4>';
			$('.messageAd').html(errorMessage).show();
		}
	});
}

function removeSettings(id) {
	var url = '/boss/settings/advert-setting/del-settings';

	var request = $.post(url, {settingsId: id},
		window.setTimeout(function () {
			location.reload()
		}, 100)
	)
}