// ĐỔI TÊN BIẾN ĐỂ TRÁNH LỖI XUNG ĐỘT TRÙNG LẶP
let headerUniqueLastScroll = 0;

window.addEventListener("DOMContentLoaded", () => {
  const header = document.querySelector(".header");
  if (!header) return;

  window.addEventListener("scroll", () => {
    let currentScroll = window.pageYOffset;

    if (currentScroll > headerUniqueLastScroll && currentScroll > 100) {
      header.classList.add("hide-header");
    } else {
      header.classList.remove("hide-header");
    }
    headerUniqueLastScroll = currentScroll;
  });
});

function openCart() {
  const cartSidebar = document.getElementById("cartSidebar");
  const cartOverlay = document.getElementById("cartOverlay");

  if (cartSidebar) cartSidebar.classList.add("active");
  if (cartOverlay) cartOverlay.classList.add("active");
}

function closeCart() {
  const cartSidebar = document.getElementById("cartSidebar");
  const cartOverlay = document.getElementById("cartOverlay");

  if (cartSidebar) cartSidebar.classList.remove("active");
  if (cartOverlay) cartOverlay.classList.remove("active");
}

const loginPopup = document.getElementById("loginPopup");
const openBtn = document.querySelector(".open-login-modal");
const closeBtn = document.querySelector(".close-login");

if (openBtn && loginPopup) {
  openBtn.addEventListener("click", function (e) {
    e.preventDefault();
    loginPopup.classList.add("show");
  });
}

if (closeBtn && loginPopup) {
  closeBtn.addEventListener("click", function () {
    loginPopup.classList.remove("show");
  });
}

if (loginPopup) {
  loginPopup.addEventListener("click", function (e) {
    if (e.target === loginPopup) {
      loginPopup.classList.remove("show");
    }
  });
}

// USER DROPDOWN
function toggleDropdown() {
  const menu = document.querySelector(".account-dropdown-menu");
  if (menu) {
    menu.classList.toggle("show");
  }
}

function loadCartPopup() {
  const cartContentDiv = document.getElementById("cartContent");

  if (!cartContentDiv) return;

  fetch("/GioHang/Popup")
    .then((response) => {
      if (!response.ok) {
        throw new Error("Lỗi tải popup giỏ hàng");
      }
      return response.text();
    })
    .then((html) => {
      cartContentDiv.innerHTML = html;
    })
    .catch((error) => {
      console.error("Lỗi loadCartPopup:", error);
    });
}

function tangSoLuong(id, btn) {
  const currentBtn =
    btn || document.querySelector(`button[onclick*="tangSoLuong(${id})"]`);

  let qtySpan = null;
  if (currentBtn) {
    const parent = currentBtn.parentElement;
    qtySpan = parent.querySelector(".qty") || parent.querySelector("span");
  }

  fetch("/GioHang/Tang/" + id)
    .then((response) => {
      if (!response.ok) throw new Error("Lỗi Server");
      return response.json();
    })
    .then((data) => {
      if (qtySpan) {
        qtySpan.textContent = data.soLuongMoi;
      } else {
        loadCartPopup();
      }

      capNhatToanBoTienGiaoDien(data.tongTienMoi);
    })
    .catch((err) => {
      console.error("Lỗi tăng số lượng:", err);
      alert("Không thể tăng số lượng!");
    });
}

// Hàm giảm số lượng
function giamSoLuong(id, btn) {
  const currentBtn =
    btn || document.querySelector(`button[onclick*="giamSoLuong(${id})"]`);

  let qtySpan = null;
  if (currentBtn) {
    const parent = currentBtn.parentElement;
    qtySpan = parent.querySelector(".qty") || parent.querySelector("span");
  }

  if (qtySpan && parseInt(qtySpan.textContent) <= 1) {
    alert("Để xóa sản phẩm, vui lòng bấm vào biểu tượng thùng rác!");
    return;
  }

  fetch("/GioHang/Giam/" + id)
    .then((response) => {
      if (!response.ok) throw new Error("Lỗi Server");
      return response.json();
    })
    .then((data) => {
      if (qtySpan) {
        qtySpan.textContent = data.soLuongMoi;
      } else {
        loadCartPopup();
      }

      capNhatToanBoTienGiaoDien(data.tongTienMoi);
    })
    .catch((err) => {
      console.error("Lỗi giảm số lượng:", err);
      alert("Không thể giảm số lượng!");
    });
}

function capNhatToanBoTienGiaoDien(tongTienMoi) {
  const formattedPrice =
    new Intl.NumberFormat("vi-VN").format(tongTienMoi) + " đ";

  const tamTinhVung = document.querySelector(".order-box .text-danger");
  const tongTienVung = document.querySelector(".total-price");

  if (tamTinhVung) tamTinhVung.textContent = formattedPrice;
  if (tongTienVung) tongTienVung.textContent = formattedPrice;

  const tongTienPopup =
    document.getElementById("tongTienPopup") ||
    document.getElementById("tongTienGioHang");
  if (tongTienPopup) {
    tongTienPopup.textContent = formattedPrice;
  }

  if (typeof loadCartPopup === "function") {
    loadCartPopup();
  }
}

window.xoaSanPhamPopupAjax = function (idChiTiet) {
  if (!confirm("Bạn có chắc chắn muốn xóa sản phẩm này khỏi giỏ hàng không?")) {
    return;
  }

  fetch("/GioHang/XoaAjax/" + idChiTiet, {
    method: "GET",
  })
    .then((response) => {
      if (!response.ok) throw new Error("Lỗi Server");
      return response.json();
    })
    .then((data) => {
      if (data.success) {
        // 1. Tải lại popup sidebar
        loadCartPopup();

        const badge =
          document.querySelector(".cart-count") ||
          document.querySelector(".cart-badge") ||
          document.getElementById("cart-badge");

        if (badge) {
          badge.innerText = data.cartCount;
        }

        // 3. Cập nhật lại tổng tiền trên UI
        capNhatToanBoTienGiaoDien(data.tongTienMoi);
      } else {
        alert("Không thể xóa: " + data.message);
      }
    })
    .catch((err) => {
      console.error("Lỗi:", err);
      alert("Đã xảy ra lỗi khi thực hiện xóa.");
    });
};
