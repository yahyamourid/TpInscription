import React from 'react'
import { useEffect, useState } from 'react'
import axios from 'axios'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faPen, faTrash, faX, faPlus, faUser, faEnvelope, faPhone, faGraduationCap, faUsers } from '@fortawesome/free-solid-svg-icons';
import Sidebar from './Sidebar';

import { PrimeReactProvider, PrimeReactContext } from 'primereact/api';
import "primereact/resources/themes/lara-light-indigo/theme.css";
import { wait } from '@testing-library/user-event/dist/utils';
import Modal from 'react-modal'
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

const Student = () => {
  const [students, setStudents] = useState([]);
  const [selectedRoles, setSelectedRoles] = useState([]);
  const [student, setStudent] = useState({
    id: "",
    roles: [],
    username: "",
    password: "",
    firstName: "",
    lastName: "",
    phone: "",
    email: "",
    filiere: {
      id: "",
      code: "",
      libelle: ""
    }
  });
  const [roles, setRoles] = useState([]);
  const [majors, setMajors] = useState([]);
  const [loading, setLoading] = useState(false);
  const [updateMode, setUpdateMode] = useState(false);
  const [id, setId] = useState(null)
  const url = 'http://localhost:8080/api/v1/students';

  const fetchStudent = async () => {
    setLoading(true);
    const rep = await axios.get(url);
    setStudents(rep.data);
    setLoading(false);
  };

  const fetchRole = async () => {
    const rep = await axios.get('http://localhost:8080/api/v1/roles');
    setRoles(rep.data);
  };

  const fetchMajor = async () => {
    const rep = await axios.get('http://localhost:8080/api/v1/filieres');
    setMajors(rep.data);
  };

  useEffect(() => {
    fetchRole();
    fetchMajor();
    fetchStudent();
  }, []);

  const handleStudent = (e) => {
    setStudent({ ...student, [e.target.name]: e.target.value });
    console.log(students)
  };

  const handleMajor = (e) => {
    const selectedMajorId = e.target.value;
    setStudent({
      ...student,
      filiere: {
        ...student.filiere,
        id: selectedMajorId
      }
    });
  };

  const handleRoleChange = (e) => {
    const roleid = e.target.value;
    if (e.target.checked) {
      setSelectedRoles((prevRoles) => [...prevRoles, roleid]);
    } else {
      setSelectedRoles((prevRoles) => prevRoles.filter((role) => role !== roleid));
    }
  };
  useEffect(() => {
    addStudentRoles();
  }, [selectedRoles]);

  const handleUpdate = async (id) => {
    const rep = await axios.get(`${url}/${id}`)
    setStudent(rep.data)
    setUpdateMode(true)
    setSelectedRoles([])
  }

  // const handleRoleUpdate = () =>{
  //   student.roles.forEach((role) => {
  //     setSelectedRoles((prevRoles) => [...prevRoles, role.id]);
  //   })
  //   const checkboxes = document.querySelectorAll('input[type="checkbox"]');
  //   checkboxes.forEach((checkbox) => {
  //     if(selectedRoles.includes(checkbox.id))
  //     checkbox.checked = true;
  //   });

  // }

  const addStudentRoles = () => {
    const rolesArray = selectedRoles.map((role) => ({
      id: role,
      name: ""
    }));
    setStudent({
      ...student,
      roles: rolesArray
    });
  };
  const reset = () => {
    setStudent({
      id: "",
      roles: [],
      username: "",
      password: "",
      firstName: "",
      lastName: "",
      phone: "",
      email: "",
      filiere: {
        id: "",
        code: "",
        libelle: ""
      }
    })
    const checkboxes = document.querySelectorAll('input[type="checkbox"]');
    checkboxes.forEach((checkbox) => {
      checkbox.checked = false;
    });
    setSelectedRoles([]);
  };

  const addStudent = async () => {
    console.log(student)
    const rep = axios.post(url,student)
    notify('added')
    reset()
    fetchStudent()
    fetchStudent()
    fetchStudent()
  };

  const updateStudent = async () => {
    console.log(student)
    const rep = await axios.put(`${url}/${student.id}`, student)
    reset()
    fetchStudent()
    notify('updated')
  }

  const deleteStudent = async() =>{
    const rep = await axios.delete(`${url}/${id}`)
    fetchStudent()
    closeModal()
    notify('deleted')
  }

  //////modal toastify
  const [modal, setModal] = useState(false)
 
  
  const showModal = (stuid) => {
    setId(stuid);
    setModal(true);
  };
  const closeModal = () => {
    setModal(false)
  }
  const notify = (op) => toast.success(`Student ${op} successfully`, {
    position: "bottom-right",
    autoClose: 2000,
    hideProgressBar: false,
    closeOnClick: true,
    pauseOnHover: true,
    draggable: true,
    progress: undefined,
    theme: "colored",

  });

  return (
    <div className='flex flex-col min-h-screen items-center font-body w-full bg-gray-300'>
      <Sidebar name={'s'}/>
      <div className='flex flex-col ml-[90px] items-center w-5/6 '>
        <p className=' m-12 text-4xl font-bold bg-white w-full py-4 pl-12 rounded-xl text-indigo-500 shadow-xl  '><span><FontAwesomeIcon className='mr-4' icon={faUsers} /></span>Student management</p>


        {/* add_update section */}
        <div className='flex flex-col w-2/3 m-4 bg-white p-5 rounded-xl justify-center items-center shadow-xl'>
          <span className='flex items-center justify-center w-11/12'>
            <input className='flex text-gray-700 outline-none border-gray-300 border py-3 pl-4 rounded-xl focus:ring-1 w-1/3 m-3'
              placeholder='firstName'
              name='firstName'
              value={student.firstName}
              onChange={handleStudent}
            />
            <input className='flex text-gray-700 outline-none border-gray-300 border py-3 pl-4 rounded-xl focus:ring-1 w-1/3 m-3'
              placeholder='lastName'
              name='lastName'
              value={student.lastName}
              onChange={handleStudent}
            />
            <input className='flex text-gray-700 outline-none border-gray-300 border py-3 pl-4 rounded-xl focus:ring-1 w-1/3 m-3'
              placeholder='@userName'
              name='username'
              value={student.username}
              onChange={handleStudent}
            />
          </span>
          <span className='flex items-center justify-center w-11/12'>
            <input className='flex text-gray-700 outline-none border-gray-300 border py-3 pl-4 rounded-xl focus:ring-1 w-1/3 m-3'
              placeholder='email'
              name='email'
              type='email'
              value={student.email}
              onChange={handleStudent}
            /><input className='flex text-gray-700 outline-none  border-gray-300 border py-3 pl-4 rounded-xl focus:ring-1 w-1/3 m-2'
              placeholder='password'
              name='password'
              type='password'
              disabled={updateMode}
              value={student.password}
              onChange={handleStudent}
            /><input className='flex text-gray-700 outline-none border-gray-300 border py-3 pl-4 rounded-xl focus:ring-1 w-1/3 m-2'
              placeholder='phone'
              name='phone'
              type='phone'
              value={student.phone}
              onChange={handleStudent}
            />
          </span>
          <span className='flex items-center justify-center w-11/12'>
            <select className='flex text-gray-700 outline-none border-gray-300 border py-3 pl-4 rounded-xl focus:ring-1 w-1/3 m-3' value={student.filiere.id} onChange={handleMajor}>
              <option className='text-gray-700'>Select major</option>
              {
                majors.map((major, index) => (
                  <option value={major.id} >{major.libelle}</option>
                ))
              }
            </select>
            <span className='flex w-2/3 m-1 items-center ml-20'>
              {roles.map((role, index) => (
                <span className='flex m-2 items-center '>
                  <input id={role.id} type="checkbox"
                    value={role.id}
                    class="w-4 h-4 text-blue-600 bg-gray-100 border-gray-300 rounded focus:ring-blue-500 dark:focus:ring-blue-600 dark:ring-offset-gray-800 focus:ring-2 dark:bg-gray-700 dark:border-gray-600"
                    onChange={handleRoleChange}
                  />

                  <label for="default-checkbox" class="ml-2 text-sm font-medium text-gray-900 dark:text-gray-700">{role.name}</label>
                </span>
              ))}
            </span>

          </span>

          {!updateMode ?
            <button className='w-1/4 ml-10' onClick={addStudent} >
              <div className=' flex items-center justify-center py-2 px-8 rounded-xl  text-white bg-indigo-500 hover:bg-indigo-600'>
                <FontAwesomeIcon icon={faPlus} beat className='mr-4' />
                <p className='text-lg font-semibold'>Add</p>
              </div>
            </button>
            :
            <button className='w-1/4 ml-10' onClick={updateStudent}  >
              <div className=' flex items-center justify-center py-2 px-8 rounded-xl  text-white bg-indigo-500 hover:bg-indigo-600'>
                <FontAwesomeIcon icon={faPen} beat className='mr-4' />
                <p className='text-lg font-semibold'>Update</p>
              </div>
            </button>
          }


        </div>

        {/* studen section */}

        <div className='grid grid-cols-3 gap-5 w-full m-8'>
          {loading ? <p>loading data</p> :

            students.map((student, index) => (
              <div className='flex flex-col rounded-xl  p-3 bg-white border border-indigo-300 border-opacity-70'>
                <span className='flex border-b border-indigo-500 border-opacity-60 w-full mb-2 items-center pb-2'>
                  <p className='text-gray-800 font-semibold text-lg mr-3'>{student.firstName} {student.lastName}</p>
                  {(student.roles).map((r, index) => (
                    <p className='text-sm bg-gray-100 text-gray-500 rounded-xl px-3 py-1 mr-3'>{r.name} </p>
                  ))}
                </span>

                <span className='flex p-1 w-full items-center'>
                  <span className='w-2/12 text-center mx-2'>
                    <p className='py-4 bg-indigo-500 rounded-full text-white text-xl font-body'>{student.firstName[0].toUpperCase()}{student.lastName[0].toUpperCase()}</p>
                  </span>

                  <span className='flex flex-col w-9/12 text-sm ml-2'>
                    <span className='flex w-full items-center'>
                      <span className='flex w-full items-center py-1'>
                        <FontAwesomeIcon icon={faUser} className='mr-2 text-indigo-500' />
                        <p>{student.username}</p>
                      </span>
                      <span className='flex w-full items-center py-1 ml-4'>
                        <FontAwesomeIcon icon={faGraduationCap} className='mr-2 text-indigo-500' />
                        <p>{student.filiere.libelle}</p>
                      </span>
                    </span>

                    <span className='flex w-full items-center py-1'>
                      <FontAwesomeIcon icon={faEnvelope} className='mr-2 text-indigo-500' />
                      <p>{student.email}</p>
                    </span>
                    <span className='flex w-full items-center py-1'>
                      <FontAwesomeIcon icon={faPhone} className='mr-2 text-indigo-500' />
                      <p>{student.phone}</p>
                    </span>

                  </span>

                  <span className='flex flex-col w-1/12 '>
                    <button onClick={() => handleUpdate(student.id)}>
                      <FontAwesomeIcon icon={faPen} className="text-gray-300 mb-8 hover:text-green-500 hover:scale-125" />
                    </button>
                    <button onClick={() =>showModal(student.id)}>
                      <FontAwesomeIcon icon={faTrash} className="text-gray-300 hover:text-red-500 hover:scale-125" />
                    </button>
                  </span>
                </span>
              </div>
            ))}

        </div>
      </div>
      <Modal
        isOpen={modal}
        onRequestClose={closeModal}
        className='flex flex-col  bg-white w-1/4 mx-auto mt-48 rounded-md shadow border-2'
      >


        <button className='flex items-center justify-end mt-3 mr-3' onClick={closeModal}><FontAwesomeIcon icon={faX} /></button>
        <p className='text-center text-2xl font-bold mt-10 mb-8 '>Do you want to delete this student ?</p>
        <div className='flex justify-center mb-10 '>
          <button onClick={closeModal} className='text-lg font-semibold mx-1 py-2 px-5 rounded-md bg-gray-100 text-black hover:bg-gray-200'>Cancel</button>
          <button onClick={deleteStudent} className='text-lg mx-2 font-semibold py-2 px-5 rounded-md bg-red-500 text-white hover:bg-red-600' >Delete</button>
        </div>

      </Modal>
      <ToastContainer
        position="bottom-right"
        autoClose={5000}
        hideProgressBar={false}
        newestOnTop={false}
        closeOnClick
        rtl={false}
        pauseOnFocusLoss
        draggable
        pauseOnHover
        theme="light"
      />
    </div>
    

  )
}

export default Student
