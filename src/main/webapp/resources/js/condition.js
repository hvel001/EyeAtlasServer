$(document).ready(function () {
    var getTemplate,
        tagTemplate,
        getValues,
        base64;

    Handlebars.registerHelper("fourth", function(count, block) {
        if(parseInt(count)%4 === 0){
            return block.fn(this);
        }
    });

    getTemplate = function(templatePath, success) {
        $.ajax({
            url: templatePath,
            cache: true,
            success: function(rawTemplate) {
                var template = Handlebars.compile(rawTemplate);
                success(template);
            }
        });
    }

    base64 = function (input) {
        if (input.files && input.files[0]) {
            var reader = new FileReader();
            reader.onload = function (e) {
                $('#prev').attr('src', e.target.result);
            }
            reader.readAsDataURL(input.files[0]);
        }
    }

    getValues = function(elements) {
        var selected = [];
        elements.each(function() {
            selected.push($(this).val());
        });
        return selected;
    }

    $("#file").change(function(){
        base64(this);
    });

    getTemplate('template/tag', function (template) {
        $.ajax({
            url: '/rest/tag',
            success: function(json) {
                $("#tags").html(template(json));
            }
        });
    });

    getTemplate('template/category', function (template) {
        Handlebars.registerPartial("recursion", template);
        $.ajax({
            url: '/rest/category',
            success: function(json) {
                $("#category").html(template({children: json}));
            }
        });
    });

    $('#upload-button').click(function () {
        var conditionUpload = {

            condition: {
                title: $('#upload-form .entity-name').val(),
                description: $('#upload-form .entity-description').val()
            },

            category: $("input[type='radio'][name='category']:checked").val(),
            tags: getValues($("input[type='checkbox']:checked")),
            image: $('#prev').attr('src')
        };

        $.ajax({
            url: '/rest/condition',
            type: 'POST',
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(conditionUpload),
            success: function(data) {
                alert(data);
            },
            fail: function(data) {
                alert(data);
            }
        });

        $('#upload-form')[0].reset();
        $('#prev').attr('src', '#');
    });

    $('#delete-button').click(function () {
            var category = {
                name: $('#delete-form .condition-name').val()
            };

            $.ajax({
                url: '/rest/condition/' + category.name,
                type: 'delete',
                success: function (data) {
                    alert(data);
                },
                error: function(data) {
                    alert(data);
                }
            });

            name: $('#delete-form .condition-name').val("");
        });
});

