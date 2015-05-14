$(document).ready(function () {

    var loadCategories = function() {
        getTemplate('template/category_display', function (template) {
            Handlebars.registerPartial("recursion", template);
            $.ajax({
                url: '/rest/category',
                success: function(json) {
                    $("#category").html(template({children: json}));
                }
            });
        });
    }

    loadCategories();


    $('#upload-button').click(function () {
        var categoryUpload = {
            parentName: $('#upload-form .category-parent').val(),
            category: {
                name: $('#upload-form .category-name').val(),
                description: $('.category-description').val(),
                children: []
            }
        };

        $.ajax({
            url: '/rest/category',
            type: 'post',
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(categoryUpload),
            success: function (data) {
                alert(data);
                loadCategories();
            },
            error: function(data) {
                alert(data);
            }
        });

        $('#upload-form .category-name').val("");
        $('#upload-form .category-parent').val("");
        $('.category-description').val("");
    });

    $('#delete-button').click(function () {
        var category = {
            name: $('#delete-form .category-name').val()
        };

        $.ajax({
            url: '/rest/category/' + category.name,
            type: 'delete',
            success: function (data) {
                alert(data);
                loadCategories();
            },
            error: function(data) {
                alert(data);
            }
        });

        $('#delete-form .category-name').val("");
    });
});

