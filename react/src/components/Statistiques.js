import React from 'react'
import { useEffect, useState } from 'react'
import axios from 'axios'

const Statistiques = () => {
    const [majors, setMajors] = useState([])
    const [major, setMajor] = useState({
        "id": 0,

    })
    const [students, setStudent] = useState([])
    const [loading, setLoading] = useState(false)
    const [loading1, setLoading1] = useState(false)
    const url = 'http://localhost:8080/api/v1/filieres';
    const url1 = 'http://localhost:8080/api/v1/students/filiere';


    const fetshMajors = async () => {
        setLoading(true)
        const rep = await axios.get(url)
        setMajors(rep.data)
        setLoading(false)
    }
    useEffect(() => {
        fetshMajors()
    }, [])

    useEffect(() => {
        fetchStudents()
    }, [major.id]);

    const handleMajor = (e) => {
        setMajor({
            "id": e.target.value,
            "code": "",
            "libelle": ""
        });
    };
    const fetchStudents = async() => {
        setLoading1(true)
        const rep = await axios.get(`${url1}/${major.id}`)
        setStudent(rep.data)
        setLoading1(false)
    }


    return (
        <div className='font-body w-1/3'>
            {!loading ?
            <div className='flex flex-col  items-center w-full'>
                <select className='flex  text-gray-700 outline-none border-indigo-300 border py-3 pl-4 rounded-xl focus:ring-1 w-full m-3' onChange={handleMajor}>
                    <option className='text-gray-700'>Select major</option>
                    {
                        majors.map((major, index) => (
                            <option value={major.id} >{major.libelle}</option>
                        ))
                    }
                </select>
                {
                    !loading1 ? 
                    (   students.length === 0 ? 
                        <p>No data to display</p>:
                        <span className='border rouunded-xl text-lg p-5 text-gray-700  rounded-xl w-full bg-white bg-opacity-70'>
                        <span className='flex items-center mb-2 bg-gray-100 p-1  '>
                                <span className='text-center font-semibold text-gray-900 w-1/5'>id</span> 
                                <span className='text-center font-semibold text-gray-900 w-2/5'>firstName</span> 
                                <span className='text-center font-semibold text-gray-900 w-2/5'>lastName</span> 
                        </span>
                        {students.map((student,index) => (
                            <span className='flex items-center mb-1 border-b w-full'>
                                <span className='text-center  w-1/5'>{student.id}</span> 
                                <span className='text-center  w-2/5'>{student.firstName}</span> 
                                <span className='text-center  w-2/5'>{student.lastName}</span> 
                        </span>
                        ))}
                    </span>):
                    <span>loading students</span>
                }
                </div>
                :
                <p>loading majors</p>
            }

        </div>
    )
}

export default Statistiques
