// $(document).ready(function() {
//     $(".change-avatar-btn").click(() => {
//         $("#avatar-input").click();
//         console.log("Bắt sự kiện click");
//     });
//
//
//     $("#avatar-input").change(event => {
//         console.log("Bắt sự kiện change");
//         const tempFiles = event.target.files;
//
//         if (!tempFiles || tempFiles.length === 0) {
//             return;
//         }
//         console.log(event.target.files);
//         chosenFile = tempFiles[0];
//
//         const imageBlob = new Blob([chosenFile], {type: chosenFile.type});
//         const imageUrl = URL.createObjectURL(imageBlob);
//         $("#avatar").attr("src", imageUrl);
//     });
// });

const btnChangeAvatar = document.getElementById('');

$(document).ready(function () {

    // toastr.options.timeOut = 2500; // 2.5s
    let chosenFile = null;

    $(".change-avatar111").click(() => {
        $("#avatar-input-test111").click();
    });

    $("#avatar-input-test111").change(event => {
        const tempFiles = event.target.files;
        if (!tempFiles || tempFiles.length === 0) {
            return;
        }
        console.log(event.target.files[0].data);
        chosenFile = tempFiles[0];

        const imageBlob = new Blob([chosenFile], {type: chosenFile.type});
        const imageUrl = URL.createObjectURL(imageBlob);
        $("#avatar-test").attr("src", imageUrl);
    });

    $(".submit-avatar-btn").click(() => {
        if (!chosenFile) {
            toastr.error("Chưa chọn file");
            return;
        }

        const formData = new FormData();
        formData.append('avatar', chosenFile);
        $.ajax({
            url: '/api/v1/users/avatar',
            data: formData,
            type: 'POST',
            contentType: false, // NEEDED, DON'T OMIT THIS (requires jQuery 1.6+)
            processData: false, // NEEDED, DON'T OMIT THIS
            success: function (data) {
                console.log(data);
                toastr.success(data);
            },
            error: function (errorData) {
                console.log(errorData);
                toastr.error(errorData);
            }
        });
    });

});