import React, { useState } from 'react';
import axios from 'axios';

const UploadForm = ({ onUpload }) => {
  const [file, setFile] = useState(null);
  const fileInputRef = React.createRef();

  const handleFileChange = (e) => {
    setFile(e.target.files[0]);
  };

  const handleUpload = async () => {
    if (file) {
      const formData = new FormData();
      formData.append('file', file);

      try {
        await axios.post('http://localhost:8080/employees/parse-csv', formData);
        onUpload(); 
      } catch (error) {
        console.error('Error uploading file:', error);
      }
    } else {
      console.error('No file selected');
    }
  };

  const handleButtonClick = () => {
    fileInputRef.current.click();
  };

  return (
    <div className="button-container">
      <input type="file" onChange={handleFileChange} ref={fileInputRef} style={{ display: 'none' }} />
      <label onClick={handleButtonClick}>Choose a file</label>
      <button onClick={handleUpload}>Upload</button>
    </div>
  );
};

export default UploadForm;
