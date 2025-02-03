<?php
header('Content-Type: application/json');

// Konfigurasi koneksi ke database
$host = 'localhost';
$username = 'root';
$password = '';
$database = 'crime_reports';  // Pastikan ini sesuai dengan nama database Anda

// Koneksi ke database
$conn = new mysqli($host, $username, $password, $database);

// Cek koneksi
if ($conn->connect_error) {
    http_response_code(500);
    die(json_encode(["status" => "error", "message" => "Connection failed: " . $conn->connect_error]));
}

// Mendapatkan data dari request
$name = trim($_POST['name'] ?? '');
$crime = trim($_POST['crime'] ?? '');
$tkp = trim($_POST['tkp'] ?? '');
$notes = trim($_POST['notes'] ?? '');
$filePath = '';

// Validasi input
if (empty($name) || empty($crime) || empty($tkp) || empty($notes)) {
    http_response_code(400); // Bad Request
    echo json_encode(["status" => "error", "message" => "All fields are required"]);
    exit;
}

// Proses file (jika ada)
if (!empty($_FILES['file']['name'])) {
    $targetDir = "uploads/";

    // Validasi ukuran file (maksimal 5MB)
    if ($_FILES['file']['size'] > 5 * 1024 * 1024) {
        http_response_code(400); // Bad Request
        echo json_encode(["status" => "error", "message" => "File size exceeds the maximum limit of 5 MB"]);
        exit;
    }

    $fileName = basename($_FILES['file']['name']);
    $fileExtension = strtolower(pathinfo($fileName, PATHINFO_EXTENSION));

    // Validasi ekstensi file
    $allowedExtensions = ['jpg', 'jpeg', 'png', 'mp4']; 
    if (!in_array($fileExtension, $allowedExtensions)) {
        http_response_code(400);
        echo json_encode(["status" => "error", "message" => "Invalid file extension"]);
        exit;
    }

    // Pastikan folder uploads sudah ada
    if (!is_dir($targetDir) && !mkdir($targetDir, 0777, true)) {
        http_response_code(500); // Internal Server Error
        echo json_encode(["status" => "error", "message" => "Failed to create upload directory"]);
        exit;
    }

    // Buat nama file baru dengan timestamp dan ekstensi
    $newFileName = time() . "_" . uniqid() . "." . $fileExtension;
    $targetFilePath = $targetDir . $newFileName;

    // Simpan file ke server
    if (move_uploaded_file($_FILES['file']['tmp_name'], $targetFilePath)) {
        $filePath = $targetFilePath; // Simpan path file gambar
    } else {
        http_response_code(500); // Internal Server Error
        echo json_encode(["status" => "error", "message" => "File upload failed"]);
        exit;
    }
}

// Query untuk memasukkan data
$sql = "INSERT INTO reports (name, crime, tkp, notes, upload_path) VALUES (?, ?, ?, ?, ?)";
$stmt = $conn->prepare($sql);

// Periksa apakah prepare berhasil
if ($stmt === false) {
    http_response_code(500); // Internal Server Error
    echo json_encode(["status" => "error", "message" => "Database error: " . $conn->error]);
    exit;
}

// Bind parameter (gunakan tipe "s" untuk string)
$stmt->bind_param("sssss", $name, $crime, $tkp, $notes, $filePath);

if ($stmt->execute()) {
    echo json_encode([
        "status" => "success",
        "message" => "Report inserted successfully",
        "id" => $stmt->insert_id
    ]);
} else {
    http_response_code(500); // Internal Server Error
    echo json_encode(["status" => "error", "message" => "Failed to insert report: " . $stmt->error]);
}

// Tutup koneksi
$stmt->close();
$conn->close();
?>
